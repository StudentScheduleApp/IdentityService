package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutlineMediaAuthorizeService extends Authorized {
    private final MemberRepository memberRepository;
    private final OutlineMediaRepository outlineMediaRepository;
    private final OutlineRepository outlineRepository;
    private final SpecificLessonRepository specificLessonRepository;
    private final CheckUtil checkUtil;

    public OutlineMediaAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, MemberRepository memberRepository, OutlineMediaRepository outlineMediaRepository, OutlineRepository outlineRepository, SpecificLessonRepository specificLessonRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.memberRepository = memberRepository;
        this.outlineMediaRepository = outlineMediaRepository;
        this.outlineRepository = outlineRepository;
        this.specificLessonRepository = specificLessonRepository;
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
    private boolean checkUserForAdmin() throws Exception {
        for(Long id : ids) {
            OutlineMedia outlineMedia = outlineMediaRepository.getById(id);
            Outline outline = outlineRepository.getById(outlineMedia.getOutlineId());
            List<Member> memberList = memberRepository.getByGroupId(
                    specificLessonRepository.getById(outline.getSpecificLessonId()).getGroupId());
            if(!checkUtil.checkUserForMemberRole(memberList,user,MemberRole.ADMIN)){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            OutlineMedia outlineMedia = outlineMediaRepository.getById(id);
            Outline outline = outlineRepository.getById(outlineMedia.getOutlineId());
            List<Member> memberList = memberRepository.getByGroupId(
                    specificLessonRepository.getById(outline.getSpecificLessonId()).getGroupId()
            );
            if(!checkUtil.checkUserForMemberRole(memberList,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return false;
    }
}
