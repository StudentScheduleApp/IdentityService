package com.studentscheduleapp.identityservice.services;

import com.studentscheduleapp.identityservice.models.*;
import com.studentscheduleapp.identityservice.models.api.AuthorizeEntity;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public boolean authorize(String token, AuthorizeEntity authorizeEntity){
        try {
            User u = userRepository.getByEmail(jwtProvider.getAccessClaims(token).getSubject());
            if (u == null)
                return false;
            if (authorizeEntity.getType().equals(AuthorizeType.GET)) {
                switch (authorizeEntity.getEntity()) {
                    case USER:
                        if (!(authorizeEntity.getIds().size() == 1 && authorizeEntity.getIds().get(0) == u.getId() && (authorizeEntity.getParams().contains("email") || authorizeEntity.getParams().contains("password"))))
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
                        Set<Long> ids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            CustomLesson cl = customLessonRepository.getById(l);
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
                        Set<Long> gids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Member cl = memberRepository.getById(l);
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
                        Set<Long> ggids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
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
                        Set<Long> gggids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
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
                        Set<Long> sids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            LessonTemplate cl = lessonTemplateRepository.getById(l);
                            sids.add(cl.getScheduleTemplateId());
                        }
                        Set<Long> ggggids = new HashSet<>();
                        for (Long l : sids) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
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
                        Set<Long> slids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Outline cl = outlineRepository.getById(l);
                            slids.add(cl.getSpecificLessonId());
                        }
                        Set<Long> gggggids = new HashSet<>();
                        for (Long l : slids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
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
                        Set<Long> oids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            oids.add(cl.getOutlineId());
                        }
                        Set<Long> sllids = new HashSet<>();
                        for (Long l : oids) {
                            Outline cl = outlineRepository.getById(l);
                            sllids.add(cl.getSpecificLessonId());
                        }
                        Set<Long> ggggggids = new HashSet<>();
                        for (Long l : sllids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
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
                        Set<Long> ocids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMediaComment cl = outlineMediaCommentRepository.getById(l);
                            ocids.add(cl.getMediaId());
                        }
                        Set<Long> ooids = new HashSet<>();
                        for (Long l : ocids) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            ooids.add(cl.getOutlineId());
                        }
                        Set<Long> ssllids = new HashSet<>();
                        for (Long l : ooids) {
                            Outline cl = outlineRepository.getById(l);
                            ssllids.add(cl.getSpecificLessonId());
                        }
                        Set<Long> gggggggids = new HashSet<>();
                        for (Long l : ssllids) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
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
                }
                return true;
            }
            if (authorizeEntity.getType().equals(AuthorizeType.CREATE)){
                switch (authorizeEntity.getEntity()) {
                    case USER:
                    case SPECIFIC_LESSON:
                        return false;
                    case GROUP:
                        return true;
                    case CUSTOM_LESSON:
                    case SCHEDULE_TEMPLATE:
                    case MEMBER:
                        for (Long l : authorizeEntity.getIds()) {
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
                        Set<Long> ggggids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
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
                        Set<Long> gggids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                             SpecificLesson sl = specificLessonRepository.getById(l);
                             gggids.add(sl.getGroupId());
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
                    case OUTLINE_MEDIA:
                        Set<Long> ggggifds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Outline sl = outlineRepository.getById(l);
                            ggggifds.add(sl.getSpecificLessonId());
                        }
                        Set<Long> ggggdifds = new HashSet<>();
                        for (Long l : ggggifds) {
                            SpecificLesson sl = specificLessonRepository.getById(l);
                            ggggdifds.add(sl.getGroupId());
                        }
                        for (Long l : ggggdifds) {
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
                        Set<Long> ggggdsifds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMedia sl = outlineMediaRepository.getById(l);
                            ggggdsifds.add(sl.getOutlineId());
                        }
                        Set<Long> ggggsifds = new HashSet<>();
                        for (Long l : ggggdsifds) {
                            Outline sl = outlineRepository.getById(l);
                            ggggsifds.add(sl.getSpecificLessonId());
                        }
                        Set<Long> ggggdifdds = new HashSet<>();
                        for (Long l : ggggsifds) {
                            SpecificLesson sl = specificLessonRepository.getById(l);
                            ggggdifdds.add(sl.getGroupId());
                        }
                        for (Long l : ggggdifdds) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                }
                return true;
            }
            if (authorizeEntity.getType().equals(AuthorizeType.PATCH)){
                switch (authorizeEntity.getEntity()) {
                    case USER:
                        if (authorizeEntity.getIds().size() == 1 && authorizeEntity.getIds().get(0) == u.getId())
                            if (authorizeEntity.getParams().contains("email") ||
                                    authorizeEntity.getParams().contains("banned") ||
                                    authorizeEntity.getParams().contains("roles"))
                                return false;
                        else if (!u.getRoles().contains(Role.ADMIN))
                            return false;
                        break;
                    case SPECIFIC_LESSON:
                        if (authorizeEntity.getParams().contains("groupId") ||
                                authorizeEntity.getParams().contains("lessonId"))
                            return false;
                        Set<Long> gggfgids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            gggfgids.add(cl.getGroupId());
                        }
                        for (Long l : gggfgids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case GROUP:
                        if (authorizeEntity.getParams().contains("groupId") ||
                            authorizeEntity.getParams().contains("lessonId"))
                            return false;
                        for (Long l : authorizeEntity.getIds()) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case CUSTOM_LESSON:
                        if (authorizeEntity.getParams().contains("groupId") ||
                                authorizeEntity.getParams().contains("lessonId"))
                            return false;
                        Set<Long> gggdfgids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            CustomLesson cl = customLessonRepository.getById(l);
                            gggdfgids.add(cl.getGroupId());
                        }
                        for (Long l : gggdfgids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case SCHEDULE_TEMPLATE:
                        if (authorizeEntity.getParams().contains("groupId") ||
                                authorizeEntity.getParams().contains("lessonId"))
                            return false;
                        Set<Long> gggdddfgids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            gggdddfgids.add(cl.getGroupId());
                        }
                        for (Long l : gggdddfgids) {
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
                        if (authorizeEntity.getParams().contains("groupId") ||
                                authorizeEntity.getParams().contains("userId"))
                            return false;
                        for (Long l : authorizeEntity.getIds()) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId() && m.getRoles().contains(MemberRole.ADMIN) && !authorizeEntity.getIds().contains(m.getId()))
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                    case LESSON_TEMPLATE:
                        if (authorizeEntity.getParams().contains("groupId") ||
                                authorizeEntity.getParams().contains("userId"))
                            return false;
                        Set<Long> ggggids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            LessonTemplate cl = lessonTemplateRepository.getById(l);
                            ggggids.add(cl.getScheduleTemplateId());
                        }
                        Set<Long> ggggdids = new HashSet<>();
                        for (Long l : ggggids) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
                            ggggdids.add(cl.getGroupId());
                        }
                        for (Long l : ggggdids) {
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
                        Set<Long> gggdids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Outline sl = outlineRepository.getById(l);
                            gggdids.add(sl.getSpecificLessonId());
                        }
                        Set<Long> gggids = new HashSet<>();
                        for (Long l : gggdids) {
                            SpecificLesson sl = specificLessonRepository.getById(l);
                            gggids.add(sl.getGroupId());
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
                    case OUTLINE_MEDIA:
                        Set<Long> gggdidds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMedia sl = outlineMediaRepository.getById(l);
                            gggdidds.add(sl.getOutlineId());
                        }
                        Set<Long> ggggifds = new HashSet<>();
                        for (Long l : gggdidds) {
                            Outline sl = outlineRepository.getById(l);
                            ggggifds.add(sl.getSpecificLessonId());
                        }
                        Set<Long> ggggdifds = new HashSet<>();
                        for (Long l : ggggifds) {
                            SpecificLesson sl = specificLessonRepository.getById(l);
                            ggggdifds.add(sl.getGroupId());
                        }
                        for (Long l : ggggdifds) {
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
                        Set<Long> gggghgdsifds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMediaComment sl = outlineMediaCommentRepository.getById(l);
                            gggghgdsifds.add(sl.getMediaId());
                        }
                        Set<Long> ggggdsifds = new HashSet<>();
                        for (Long l : gggghgdsifds) {
                            OutlineMedia sl = outlineMediaRepository.getById(l);
                            ggggdsifds.add(sl.getOutlineId());
                        }
                        Set<Long> ggggsifds = new HashSet<>();
                        for (Long l : ggggdsifds) {
                            Outline sl = outlineRepository.getById(l);
                            ggggsifds.add(sl.getSpecificLessonId());
                        }
                        Set<Long> ggggdifdds = new HashSet<>();
                        for (Long l : ggggsifds) {
                            SpecificLesson sl = specificLessonRepository.getById(l);
                            ggggdifdds.add(sl.getGroupId());
                        }
                        for (Long l : ggggdifdds) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                }
                return true;
            }
            if (authorizeEntity.getType().equals(AuthorizeType.DELETE)){
                switch (authorizeEntity.getEntity()) {
                    case USER:
                    case GROUP:
                    case SPECIFIC_LESSON:
                        return false;
                    case CUSTOM_LESSON:
                        Set<Long> ids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            CustomLesson cl = customLessonRepository.getById(l);
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
                        Set<Long> gids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Member cl = memberRepository.getById(l);
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
                    case SCHEDULE_TEMPLATE:
                        Set<Long> gggids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
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
                        Set<Long> sids = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            LessonTemplate cl = lessonTemplateRepository.getById(l);
                            sids.add(cl.getScheduleTemplateId());
                        }
                        Set<Long> ggggids = new HashSet<>();
                        for (Long l : sids) {
                            ScheduleTemplate cl = scheduleTemplateRepository.getById(l);
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
                        Set<Long> sidds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            Outline cl = outlineRepository.getById(l);
                            sidds.add(cl.getSpecificLessonId());
                        }
                        Set<Long> ggghgids = new HashSet<>();
                        for (Long l : sidds) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            ggghgids.add(cl.getGroupId());
                        }
                        for (Long l : ggghgids) {
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
                        Set<Long> sidgds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            sidgds.add(cl.getOutlineId());
                        }
                        Set<Long> siddgds = new HashSet<>();
                        for (Long l : sidgds) {
                            Outline cl = outlineRepository.getById(l);
                            siddgds.add(cl.getSpecificLessonId());
                        }
                        Set<Long> ggghgdids = new HashSet<>();
                        for (Long l : siddgds) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            ggghgdids.add(cl.getGroupId());
                        }
                        for (Long l : ggghgdids) {
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
                        Set<Long> sidggdds = new HashSet<>();
                        for (Long l : authorizeEntity.getIds()) {
                            OutlineMediaComment cl = outlineMediaCommentRepository.getById(l);
                            sidggdds.add(cl.getMediaId());
                        }
                        Set<Long> sidgdds = new HashSet<>();
                        for (Long l : sidggdds) {
                            OutlineMedia cl = outlineMediaRepository.getById(l);
                            sidgdds.add(cl.getOutlineId());
                        }
                        Set<Long> sidddgds = new HashSet<>();
                        for (Long l : sidgdds) {
                            Outline cl = outlineRepository.getById(l);
                            sidddgds.add(cl.getSpecificLessonId());
                        }
                        Set<Long> ggghgfdids = new HashSet<>();
                        for (Long l : sidddgds) {
                            SpecificLesson cl = specificLessonRepository.getById(l);
                            ggghgfdids.add(cl.getGroupId());
                        }
                        for (Long l : ggghgfdids) {
                            boolean fl = false;
                            for (Member m : memberRepository.getByGroupId(l)) {
                                if (m.getUserId() == u.getId())
                                    fl = true;
                            }
                            if (!fl)
                                return false;
                        }
                        break;
                }
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
