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
public class OutlineAuthorizeService extends Authorized {
    private final OutlineRepository outlineRepository;
    private final SpecificLessonRepository specificLessonRepository;
    private final MemberRepository memberRepository;
    private final CheckUtil checkUtil;
    private static final Logger log = LogManager.getLogger(OutlineAuthorizeService.class);

    public OutlineAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, OutlineRepository outlineRepository, SpecificLessonRepository specificLessonRepository, MemberRepository memberRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.outlineRepository = outlineRepository;
        this.specificLessonRepository = specificLessonRepository;
        this.memberRepository = memberRepository;
        this.checkUtil = checkUtil;
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            if(!checkUserForOutlineOwner() && !checkUserForAdmin() && !user.getRoles().contains(Role.ADMIN))
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
            if (params.contains("id") || params.contains("userId") || params.contains("specificLessonId"))
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
            return checkUserForMember();
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
    private boolean checkUserForOutlineOwner() throws Exception {
        if(user.getRoles().contains(Role.ADMIN)){
            return true;
        }
        for(Long id : ids){
            Outline outline = outlineRepository.getById(id);
            if(outline == null)
                continue;
            if(outline.getUserId() == user.getId()){
                return true;
            }
        }
        return false;
    }
    private boolean checkUserForAdmin() throws Exception {
        for(Long id : ids){
            Outline outline = outlineRepository.getById(id);
            if(outline == null)
                continue;
            SpecificLesson specificLesson = specificLessonRepository.getById(outline.getSpecificLessonId());
            List<Member> members = memberRepository.getByGroupId(specificLesson.getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user, MemberRole.ADMIN)){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            Outline outline = outlineRepository.getById(id);
            if(outline == null)
                continue;
            SpecificLesson specificLesson = specificLessonRepository.getById(outline.getSpecificLessonId());
            List<Member> members = memberRepository.getByGroupId(specificLesson.getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }

}
