package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.AuthorizeType;
import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserAuthorizeService extends Authorized {
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

    public UserAuthorizeService(UserRepository ur, JwtProvider jwtProvider) {
        super(ur, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            return user.getRoles().contains(Role.ULTIMATE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
       // return true;
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if(params.contains("id") || params.contains("email"))
                return false;
            if((params.contains("password") || params.contains("firstName") || params.contains("lastName") || params.contains("avaUrl")) && (ids.size() != 1 || !ids.contains(user.getId())))
                return false;
            if(params.contains("banned") && (!user.getRoles().contains(Role.ADMIN) || ids.contains(user.getId())))
                return false;
            if (params.contains("roles") && !user.getRoles().contains(Role.ULTIMATE))
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
            if (!user.getRoles().contains(Role.ULTIMATE))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected boolean authorizeGet() {
        try {
            if (!(ids.size() == 1 && ids.contains(user.getId()) || user.getRoles().contains(Role.ULTIMATE)) && (params.contains("email") || params.contains("password")))
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
