package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleTemplateAuthorizeService extends Authorized {
    private final MemberRepository memberRepository;
    private final ScheduleTemplateRepository scheduleTemplateRepository;
    private final CheckUtil checkUtil;

    public ScheduleTemplateAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, MemberRepository memberRepository, ScheduleTemplateRepository scheduleTemplateRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.memberRepository = memberRepository;
        this.scheduleTemplateRepository = scheduleTemplateRepository;
        this.checkUtil = checkUtil;
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
            if (params.contains("id") || params.contains("groupId"))
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
        for(Long id : ids){
            List<Member> members = memberRepository.getByGroupId(scheduleTemplateRepository.getById(id).getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user, MemberRole.ADMIN)){
                return false;
            }
        }
        return true;

    }private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            List<Member> members = memberRepository.getByGroupId(scheduleTemplateRepository.getById(id).getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }
}
