package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.AuthorizeType;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OutlineMediaCommentAuthorizeService extends Authorized {
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

    public OutlineMediaCommentAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider) {
        super(userRepository, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizePatch() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizeCreate() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizeGet() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
