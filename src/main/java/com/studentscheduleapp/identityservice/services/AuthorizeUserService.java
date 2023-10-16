package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.OutlineMediaComment;
import com.studentscheduleapp.identityservice.models.api.AuthorizeEntity;
import com.studentscheduleapp.identityservice.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeUserService {

    @Autowired
    private CustomLessonRepository customLessonRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private LessonTemplateRepository lessonTemplateRepository;
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

    public boolean authorize(String token, AuthorizeEntity authorizeEntity){







        return true;
    }

}
