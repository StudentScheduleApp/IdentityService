package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Group;
import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupAuthorizeService extends Authorized {
    @Autowired
    private CustomLessonRepository customLessonRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private LessonTemplateRepository lessonTemplateRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OutlineMediaCommentRepository outlineMediaCommentRepository;
    @Autowired
    private OutlineMediaRepository outlineMediaRepository;
    @Autowired
    private OutlineRepository outlineRepository;
    @Autowired
    private ScheduleTemplateRepository scheduleTemplateRepository;
    @Autowired
    private SpecificLessonRepository specificLessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CheckUtil checkUtil;

    public GroupAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider) {
        super(userRepository, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            return memberRepository.getByUserId(user.getId()).size() > 0 &&
                    user.getRoles().contains(Role.ULTIMATE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizeCreate() {
        try {
            return user.getRoles().contains(Role.USER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizeGet() {
        try {
            return memberRepository.getByUserId(user.getId()).size() > 0 &&
                    user.getRoles().contains(Role.USER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean checkUserForAdmin() throws Exception {
        if(user.getRoles().contains(Role.ADMIN)){
            return true;
        }
        int validatedEntities = 0;
        for(Long id : ids){
            List<Member> groupMemberList = memberRepository.getByGroupId(id);
            if(checkUtil.checkUserForMemberRole(groupMemberList,user, MemberRole.ADMIN)){
                validatedEntities +=1;
            }
        }
        return validatedEntities == ids.size();
    }
}
