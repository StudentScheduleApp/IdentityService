package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.models.api.AuthorizeEntity;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.*;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorizeUserService {

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
    private JwtProvider jwtProvider;
    @Autowired
    private CheckUtil checkUtil;

    public boolean authorize(String token, AuthorizeEntity authorizeEntity) {
        Authorized auth;
        switch (authorizeEntity.getEntity()) {
            case CUSTOM_LESSON:
                auth = new CustomLessonAuthorizeService(userRepository, jwtProvider, customLessonRepository, memberRepository, checkUtil);
                break;
            case GROUP:
                auth = new GroupAuthorizeService(userRepository, jwtProvider, memberRepository, checkUtil);
                break;
            case LESSON_TEMPLATE:
                auth = new LessonTemplateAuthorizeService(userRepository, jwtProvider, lessonTemplateRepository, memberRepository, scheduleTemplateRepository, checkUtil);
                break;
            case MEMBER:
                auth = new MemberAuthorizeService(userRepository, jwtProvider, memberRepository, checkUtil);
                break;
            case OUTLINE_MEDIA_COMMENT:
                auth = new OutlineMediaCommentAuthorizeService(userRepository, jwtProvider, memberRepository, outlineMediaCommentRepository, outlineMediaRepository, outlineRepository, specificLessonRepository, checkUtil);
                break;
            case OUTLINE_MEDIA:
                auth = new OutlineMediaAuthorizeService(userRepository, jwtProvider, memberRepository, outlineMediaRepository, outlineRepository, specificLessonRepository, checkUtil);
                break;
            case OUTLINE:
                auth = new OutlineAuthorizeService(userRepository, jwtProvider, outlineRepository, specificLessonRepository, memberRepository, checkUtil);
                break;
            case SCHEDULE_TEMPLATE:
                auth = new ScheduleTemplateAuthorizeService(userRepository, jwtProvider, memberRepository, scheduleTemplateRepository, checkUtil);
                break;
            case SPECIFIC_LESSON:
                auth = new SpecificLessonAuthorizeService(userRepository, jwtProvider, memberRepository, specificLessonRepository, checkUtil);
                break;
            case USER:
                auth = new UserAuthorizeService(userRepository, jwtProvider);
                break;
            default:
                auth = null;
        }
        if (auth == null)
            return false;
        auth.init(token, authorizeEntity.getType(), authorizeEntity.getIds(), authorizeEntity.getParams());
        return auth.authorize();


    }
}
