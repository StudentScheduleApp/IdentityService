package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

@Service
public class CustomLessonAuthorizeService extends Authorized {
    private final CustomLessonRepository customLessonRepository;
    private final MemberRepository memberRepository;
    private final CheckUtil checkUtil;

    private static final Logger log = LogManager.getLogger(CustomLessonAuthorizeService.class);
    public CustomLessonAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, CustomLessonRepository customLessonRepository, MemberRepository memberRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.customLessonRepository = customLessonRepository;
        this.memberRepository = memberRepository;
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
            List<Member> members = memberRepository.getByGroupId(customLessonRepository.getById(id).getGroupId());
            if(members == null || members.isEmpty())
                continue;
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.ADMIN)){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            List<Member> members = memberRepository.getByGroupId(customLessonRepository.getById(id).getGroupId());
            if(members == null || members.isEmpty())
                continue;
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }
}
