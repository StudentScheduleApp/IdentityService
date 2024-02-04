package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutlineMediaCommentAuthorizeService extends Authorized {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OutlineMediaCommentRepository outlineMediaCommentRepository;
    @Autowired
    private OutlineMediaRepository outlineMediaRepository;
    @Autowired
    private OutlineRepository outlineRepository;
    @Autowired
    private SpecificLessonRepository specificLessonRepository;
    @Autowired
    private CheckUtil checkUtil;

    public OutlineMediaCommentAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider) {
        super(userRepository, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            return checkUserForAdmin() || checkUserForCommentOwner();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            return checkUserForAdmin() || checkUserForCommentOwner();
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
    private boolean checkUserForCommentOwner() throws Exception {
        for(Long id : ids){
            OutlineMediaComment outlineMediaComment = outlineMediaCommentRepository.getById(id);
            if(outlineMediaComment.getUserId() != user.getId()){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForAdmin() throws Exception {
        for(Long id : ids){
            OutlineMediaComment outlineMediaComment = outlineMediaCommentRepository.getById(id);
            OutlineMedia outlineMedia = outlineMediaRepository.getById(outlineMediaComment.getMediaId());
            Outline outline = outlineRepository.getById(outlineMedia.getOutlineId());
            SpecificLesson specificLesson = specificLessonRepository.getById(outline.getSpecificLessonId());
            List<Member> memberList = memberRepository.getByGroupId(specificLesson.getGroupId());
            if(!checkUtil.checkUserForMemberRole(memberList,user,MemberRole.ADMIN)){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            OutlineMediaComment outlineMediaComment = outlineMediaCommentRepository.getById(id);
            OutlineMedia outlineMedia = outlineMediaRepository.getById(outlineMediaComment.getMediaId());
            Outline outline = outlineRepository.getById(outlineMedia.getOutlineId());
            SpecificLesson specificLesson = specificLessonRepository.getById(outline.getSpecificLessonId());
            List<Member> memberList = memberRepository.getByGroupId(specificLesson.getGroupId());
            if(!checkUtil.checkUserForMemberRole(memberList,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }
}
