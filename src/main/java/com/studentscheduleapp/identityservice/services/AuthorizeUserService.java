package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.models.api.AuthorizeEntity;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    private OutlineMediaComment outlineMediaComment;
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

    public boolean authorize(String token, AuthorizeEntity authorizeEntity){
        try {
            User u = userRepository.getByEmail(jwtProvider.getAccessClaims(token).getSubject());
            if (u == null)
                return false;
            switch (authorizeEntity.getEntity()){
                case "user":
                    if (authorizeEntity.getIds().size() == 1 && authorizeEntity.getIds().get(0) == u.getId() && (authorizeEntity.getParams().contains("email") || authorizeEntity.getParams().contains("password")))
                        return false;
                    if (authorizeEntity.getParams().contains("email") || authorizeEntity.getParams().contains("password"))
                        return false;
                    break;
                case "group":
                    for (Long l : authorizeEntity.getIds()){
                        boolean fl = false;
                        for (Member m : memberRepository.getByGroupId(l)){
                            if (m.getUserId() == u.getId())
                                fl = true;
                        }
                        if (!fl)
                            return false;
                    }
                    break;
                case "customLesson":
                    ArrayList<Long> ids = new ArrayList<>();
                    for (Long l : authorizeEntity.getIds()){
                        CustomLesson cl = customLessonRepository.getById(l);
                        if (!ids.contains(cl.getGroupId()))
                            ids.add(cl.getGroupId());
                    }
                    for (Long l : ids){
                        boolean fl = false;
                        for (Member m : memberRepository.getByGroupId(l)){
                            if (m.getUserId() == u.getId())
                                fl = true;
                        }
                        if (!fl)
                            return false;
                    }
                    break;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
