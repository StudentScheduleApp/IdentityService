package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.repos.MemberRepository;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupAuthorizeService extends Authorized {
    private static final Logger log = LogManager.getLogger(GroupAuthorizeService.class);
    private final MemberRepository memberRepository;
    private final CheckUtil checkUtil;

    public GroupAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, MemberRepository memberRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.memberRepository = memberRepository;
        this.checkUtil = checkUtil;
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            if (user.getRoles().contains(Role.ULTIMATE))
                return true;
            for (Long l : ids) {
                List<Member> ms = memberRepository.getByGroupId(l);
                if (ms == null || ms.isEmpty())
                    continue;
                Member m = ms.stream().filter(i -> i.getUserId() == user.getId()).findFirst().get();
                if (!m.getRoles().contains(MemberRole.OWNER))
                    return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if (params.contains("id") || params.contains("chatId"))
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
            return true;
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
        for (Long id : ids) {
            List<Member> groupMemberList = memberRepository.getByGroupId(id);
            if (groupMemberList == null || groupMemberList.isEmpty())
                continue;
            if (!checkUtil.checkUserForMemberRole(groupMemberList, user, MemberRole.ADMIN)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkUserForMember() throws Exception {
        for (Long id : ids) {
            List<Member> members = memberRepository.getByGroupId(id);
            if (members == null || members.isEmpty())
                continue;
            if (!checkUtil.checkUserForMemberRole(members, user, MemberRole.MEMBER)) {
                return false;
            }
        }
        return true;
    }
}
