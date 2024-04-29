package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.repos.LessonTemplateRepository;
import com.studentscheduleapp.identityservice.repos.MemberRepository;
import com.studentscheduleapp.identityservice.repos.ScheduleTemplateRepository;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LessonTemplateAuthorizeService extends Authorized {
    private static final Logger log = LogManager.getLogger(LessonTemplateAuthorizeService.class);
    private final LessonTemplateRepository lessonTemplateRepository;
    private final MemberRepository memberRepository;
    private final ScheduleTemplateRepository scheduleTemplateRepository;
    private final CheckUtil checkUtil;

    public LessonTemplateAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, LessonTemplateRepository lessonTemplateRepository, MemberRepository memberRepository, ScheduleTemplateRepository scheduleTemplateRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.lessonTemplateRepository = lessonTemplateRepository;
        this.memberRepository = memberRepository;
        this.scheduleTemplateRepository = scheduleTemplateRepository;
        this.checkUtil = checkUtil;
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            if (!checkUserForAdmin() && !user.getRoles().contains(Role.ADMIN))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if (params.contains("id") || params.contains("scheduleTemplateId"))
                return false;
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizeCreate() {
        try {
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizeGet() {
        try {
            return checkUserForMember();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkUserForAdmin() throws Exception {
        for (Long id : ids) {
            LessonTemplate lessonTemplate = lessonTemplateRepository.getById(id);
            if (lessonTemplate == null)
                continue;
            ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.getById(lessonTemplate.getScheduleTemplateId());
            List<Member> members = memberRepository.getByGroupId(scheduleTemplate.getGroupId());
            if (!checkUtil.checkUserForMemberRole(members, user, MemberRole.ADMIN)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkUserForMember() throws Exception {
        for (Long id : ids) {
            LessonTemplate lessonTemplate = lessonTemplateRepository.getById(id);
            if (lessonTemplate == null)
                continue;
            ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.getById(lessonTemplate.getScheduleTemplateId());
            List<Member> members = memberRepository.getByGroupId(scheduleTemplate.getGroupId());
            if (!checkUtil.checkUserForMemberRole(members, user, MemberRole.MEMBER)) {
                return false;
            }
        }
        return true;
    }
}
