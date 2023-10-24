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

    public boolean authorize(String token, AuthorizeEntity authorizeEntity){
        try {
            User u = userRepository.getByEmail(jwtProvider.getAccessClaims(token).getSubject());
            if (u == null)
                return false;
            if (authorizeEntity.getType().equals(AuthorizeType.GET)) {
                switch (authorizeEntity.getEntity()) {
                    case USER:
                        if (authorizeEntity.getIds().size() == 1 && authorizeEntity.getIds().get(0) == u.getId() && (authorizeEntity.getParams().contains("email") || authorizeEntity.getParams().contains("password")))
                            return false;
                        if (authorizeEntity.getParams().contains("email") || authorizeEntity.getParams().contains("password"))
                            return false;
                        break;
                    case GROUP:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        for (Long l : authorizeEntity.getIds()) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case CUSTOM_LESSON:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> ids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            CustomLesson cl = customLessonRepository.getById(l);
                            if (!ids.contains(cl.getGroupId()))
                                ids.add(cl.getGroupId());
                        }
                        for (Long l : ids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case MEMBER:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> gids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Member cl = memberRepository.getById(l);
                            if (!gids.contains(cl.getGroupId()))
                                gids.add(cl.getGroupId());
                        }
                        for (Long l : gids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case SPECIFIC_LESSON:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> ggids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!ggids.contains(cl.getGroupId()))
                                ggids.add(cl.getGroupId());
                        }
                        for (Long l : ggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case SCHEDULE_TEMPLATE:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> gggids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            if (!gggids.contains(cl.getGroupId()))
                                gggids.add(cl.getGroupId());
                        }
                        for (Long l : gggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case LESSON_TEMPLATE:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> sids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            LessonTemplate cl = lessonTemplateRepository.getById(l);
                            if (!sids.contains(cl.getScheduleTemplateId()))
                                sids.add(cl.getScheduleTemplateId());
                        }
                        ArrayList<Long> ggggids = new ArrayList<>();
                        for (Long l : sids) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            if (!ggggids.contains(cl.getGroupId()))
                                ggggids.add(cl.getGroupId());
                        }
                        for (Long l : ggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case OUTLINE:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> slids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Outline cl = outlineRepository.getById(l);
                            if (!slids.contains(cl.getSpecificLessonId()))
                                slids.add(cl.getSpecificLessonId());
                        }
                        ArrayList<Long> gggggids = new ArrayList<>();
                        for (Long l : slids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!gggggids.contains(cl.getGroupId()))
                                gggggids.add(cl.getGroupId());
                        }
                        for (Long l : gggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case OUTLINE_MEDIA:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> oids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            if (!oids.contains(cl.getOutlineId()))
                                oids.add(cl.getOutlineId());
                        }
                        ArrayList<Long> sllids = new ArrayList<>();
                        for (Long l : oids) {
                            Outline cl = outlineRepository.getById(l);
                            if (!sllids.contains(cl.getSpecificLessonId()))
                                sllids.add(cl.getSpecificLessonId());
                        }
                        ArrayList<Long> ggggggids = new ArrayList<>();
                        for (Long l : sllids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!ggggggids.contains(cl.getGroupId()))
                                ggggggids.add(cl.getGroupId());
                        }
                        for (Long l : ggggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case OUTLINE_MEDIA_COMMENT:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> ocids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMediaComment cl = outlineMediaCommentRepository.getById(l);
                            if (!ocids.contains(cl.getMediaId()))
                                ocids.add(cl.getMediaId());
                        }
                        ArrayList<Long> ooids = new ArrayList<>();
                        for (Long l : ocids) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            if (!ooids.contains(cl.getOutlineId()))
                                ooids.add(cl.getOutlineId());
                        }
                        ArrayList<Long> ssllids = new ArrayList<>();
                        for (Long l : ooids) {
                            Outline cl = outlineRepository.getById(l);
                            if (!ssllids.contains(cl.getSpecificLessonId()))
                                ssllids.add(cl.getSpecificLessonId());
                        }
                        ArrayList<Long> gggggggids = new ArrayList<>();
                        for (Long l : ssllids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!gggggggids.contains(cl.getGroupId()))
                                gggggggids.add(cl.getGroupId());
                        }
                        for (Long l : gggggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case IMAGE:
                        return true;
                }
                return false;
            }
            if (authorizeEntity.getType().equals(AuthorizeType.CREATE)){
                switch (authorizeEntity.getEntity()) {
                    case USER:
                        return false;
                     //   break;
                    case GROUP:
                        return true;
                     //   break;
                    case CUSTOM_LESSON:
                        ArrayList<Long> ids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            CustomLesson cl = customLessonRepository.getById(l);
                            if (!ids.contains(cl.getGroupId()))
                                ids.add(cl.getGroupId());
                        }
                        for (Long l : ids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case MEMBER:
                        ArrayList<Long> gids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Member cl = memberRepository.getById(l);
                            if (!gids.contains(cl.getGroupId()))
                                gids.add(cl.getGroupId());
                        }
                        for (Long l : gids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                                if (m.getUserId() == u.getId() && authorizeEntity.getIds().contains(m.getId())){
                                    fl = true;
                                }
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case SPECIFIC_LESSON:
                        return false;
                     //   break;
                    case SCHEDULE_TEMPLATE:
                        ArrayList<Long> gggids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            if (!gggids.contains(cl.getGroupId()))
                                gggids.add(cl.getGroupId());
                        }
                        for (Long l : gggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case LESSON_TEMPLATE:
                        ArrayList<Long> sids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            LessonTemplate cl = lessonTemplateRepository.getById(l);
                            if (!sids.contains(cl.getScheduleTemplateId()))
                                sids.add(cl.getScheduleTemplateId());
                        }
                        ArrayList<Long> ggggids = new ArrayList<>();
                        for (Long l : sids) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            if (!ggggids.contains(cl.getGroupId()))
                                ggggids.add(cl.getGroupId());
                        }
                        for (Long l : ggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case OUTLINE:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> slids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Outline cl = outlineRepository.getById(l);
                            if (!slids.contains(cl.getSpecificLessonId()))
                                slids.add(cl.getSpecificLessonId());
                        }
                        ArrayList<Long> gggggids = new ArrayList<>();
                        for (Long l : slids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!gggggids.contains(cl.getGroupId()))
                                gggggids.add(cl.getGroupId());
                        }
                        for (Long l : gggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case OUTLINE_MEDIA:
                        if (u.getRoles().contains(Role.ADMIN))
                            return true;
                        ArrayList<Long> oids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            if (!oids.contains(cl.getOutlineId()))
                                oids.add(cl.getOutlineId());
                        }
                        ArrayList<Long> sllids = new ArrayList<>();
                        for (Long l : oids) {
                            Outline cl = outlineRepository.getById(l);
                            if (!sllids.contains(cl.getSpecificLessonId()))
                                sllids.add(cl.getSpecificLessonId());
                        }
                        ArrayList<Long> ggggggids = new ArrayList<>();
                        for (Long l : sllids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!ggggggids.contains(cl.getGroupId()))
                                ggggggids.add(cl.getGroupId());
                        }
                        for (Long l : ggggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case OUTLINE_MEDIA_COMMENT:
                        ArrayList<Long> ocids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMediaComment cl = outlineMediaCommentRepository.getById(l);
                            if (!ocids.contains(cl.getMediaId()))
                                ocids.add(cl.getMediaId());
                        }
                        ArrayList<Long> ooids = new ArrayList<>();
                        for (Long l : ocids) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            if (!ooids.contains(cl.getOutlineId()))
                                ooids.add(cl.getOutlineId());
                        }
                        ArrayList<Long> ssllids = new ArrayList<>();
                        for (Long l : ooids) {
                            Outline cl = outlineRepository.getById(l);
                            if (!ssllids.contains(cl.getSpecificLessonId()))
                                ssllids.add(cl.getSpecificLessonId());
                        }
                        ArrayList<Long> gggggggids = new ArrayList<>();
                        for (Long l : ssllids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            if (!gggggggids.contains(cl.getGroupId()))
                                gggggggids.add(cl.getGroupId());
                        }
                        for (Long l : gggggggids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case IMAGE:
                        ArrayList<Long> ggghids = new ArrayList<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            if (!ggghids.contains(cl.getGroupId()))
                                ggghids.add(cl.getGroupId());
                        }
                        for (Long l : ggghids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        return true;
                }
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
