package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.models.ScheduleTemplate;
import com.studentscheduleapp.identityservice.models.SpecificLesson;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

@Service
public class SpecificLessonAuthorizeService extends Authorized {
    private final MemberRepository memberRepository;
    private final SpecificLessonRepository specificLessonRepository;
    private final CheckUtil checkUtil;
    private static final Logger log = LogManager.getLogger(SpecificLessonAuthorizeService.class);

    public SpecificLessonAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, MemberRepository memberRepository, SpecificLessonRepository specificLessonRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.memberRepository = memberRepository;
        this.specificLessonRepository = specificLessonRepository;
        this.checkUtil = checkUtil;
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if (params.contains("id") || params.contains("groupId") || params.contains("lessonId") || params.contains("time"))
                return false;
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizeCreate() {
        try {
            return false;
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
        for(Long id : ids){
            SpecificLesson st = specificLessonRepository.getById(id);
            if(st == null)
                continue;
            List<Member> members = memberRepository.getByGroupId(st.getGroupId());
            if(members == null || members.isEmpty())
                continue;
            if(!checkUtil.checkUserForMemberRole(members,user, MemberRole.ADMIN)){
                return false;
            }
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        for(Long id : ids){
            SpecificLesson st = specificLessonRepository.getById(id);
            if(st == null)
                continue;
            List<Member> members = memberRepository.getByGroupId(st.getGroupId());
            if(members == null || members.isEmpty())
                continue;
            if(!checkUtil.checkUserForMemberRole(members,user,MemberRole.MEMBER)){
                return false;
            }
        }
        return true;
    }
}
