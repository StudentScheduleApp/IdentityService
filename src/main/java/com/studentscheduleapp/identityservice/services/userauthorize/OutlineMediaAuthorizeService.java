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
public class OutlineMediaAuthorizeService extends Authorized {
    private final MemberRepository memberRepository;
    private final OutlineMediaRepository outlineMediaRepository;
    private final OutlineRepository outlineRepository;
    private final SpecificLessonRepository specificLessonRepository;
    private final CheckUtil checkUtil;
    private static final Logger log = LogManager.getLogger(OutlineMediaAuthorizeService.class);

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
            if(!checkUserForAdmin() && !user.getRoles().contains(Role.ADMIN))
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
            if(params.contains("id") || params.contains("timestamp") || params.contains("outlineId"))
                return false;
            if(!checkUserForAdmin() && !user.getRoles().contains(Role.ADMIN))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            if(outlineMedia == null)
                continue;
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
            if(outlineMedia == null)
                continue;
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
