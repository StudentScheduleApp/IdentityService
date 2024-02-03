package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OutlineAuthorizeService extends Authorized {
    @Autowired
    private OutlineRepository outlineRepository;
    @Autowired
    private SpecificLessonRepository specificLessonRepository;
    private CheckUtil checkUtil;

    public OutlineAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider) {
        super(userRepository, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            for (Long id : ids) {
                if (checkUserForOutlineOwner() && !checkUserForAdmin())
                    return false;

            }
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if (params.contains("id") || params.contains("userId") || params.contains("specificLessonId"))
                return false;
            for (Long id : ids) {
                if (checkUserForOutlineOwner() && !checkUserForAdmin())
                    return false;

            }
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
    private boolean checkUserForOutlineOwner(){
        if(user.getRoles().contains(Role.ADMIN)){
            return true;
        }
        int validatedEntities = 0;
        for(Long id : ids){
            if(id == user.getId()){
                validatedEntities += 1;
            }
        }
        return validatedEntities == ids.size();
    }
    private boolean checkUserForAdmin() throws Exception {
        for(Long id : ids){
            Outline outline = outlineRepository.getById(id);
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
            SpecificLesson specificLesson = specificLessonRepository.getById(outline.getSpecificLessonId());
            List<Member> members = memberRepository.getByGroupId(specificLesson.getGroupId());
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }

}
