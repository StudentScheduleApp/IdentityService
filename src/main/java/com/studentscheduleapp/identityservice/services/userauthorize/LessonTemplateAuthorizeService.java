package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LessonTemplateAuthorizeService extends Authorized {
    @Autowired
    private LessonTemplateRepository lessonTemplateRepository;
    @Autowired
    private MemberRepository memberRepository;
    private ScheduleTemplateRepository scheduleTemplateRepository;
    private CheckUtil checkUtil;

    public LessonTemplateAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider) {
        super(userRepository, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if (params.contains("id") || params.contains("scheduleTemplateId"))
                return false;
            List<Member> members = memberRepository.getByUserId(user.getId());
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
        for(Long id : ids){
            LessonTemplate lessonTemplate = lessonTemplateRepository.getById(id);
            ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.getById(lessonTemplate.getScheduleTemplateId());
            List<Member> members = memberRepository.getByGroupId(scheduleTemplate.getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.ADMIN)){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            LessonTemplate lessonTemplate = lessonTemplateRepository.getById(id);
            ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.getById(lessonTemplate.getScheduleTemplateId());
            List<Member> members = memberRepository.getByGroupId(scheduleTemplate.getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }
}
