package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.repos.MemberRepository;
import com.studentscheduleapp.identityservice.repos.UserRepository;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberAuthorizeService extends Authorized {
    private static final Logger log = LogManager.getLogger(MemberAuthorizeService.class);
    private final MemberRepository memberRepository;
    private final CheckUtil checkUtil;

    public MemberAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider, MemberRepository memberRepository, CheckUtil checkUtil) {
        super(userRepository, jwtProvider);
        this.memberRepository = memberRepository;
        this.checkUtil = checkUtil;
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            List<Member> members = memberRepository.getByUserId(user.getId());
            for (Long id : ids) {
                Member member = memberRepository.getById(id);
                if (member == null)
                    continue;
                if (member.getRoles().contains(MemberRole.OWNER))
                    return false;
                Member m = members.stream().filter(i -> i.getGroupId() == member.getGroupId()).findFirst().get();
                if (member.getRoles().contains(MemberRole.ADMIN) && !m.getRoles().contains(MemberRole.OWNER))
                    return false;

            }
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizePatch() {
        try {
            if (params.contains("id") || params.contains("userId") || params.contains("groupId"))
                return false;
            List<Member> members = memberRepository.getByUserId(user.getId());
            for (Long id : ids) {
                Member member = memberRepository.getById(id);
                if (member == null)
                    continue;
                if (member.getRoles().contains(MemberRole.OWNER))
                    return false;
                Member m = members.stream().filter(i -> i.getGroupId() == member.getGroupId()).findFirst().get();
                if (member.getRoles().contains(MemberRole.ADMIN) && !m.getRoles().contains(MemberRole.OWNER))
                    return false;

            }
            return checkUserForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected boolean authorizeCreate() {
        try {
            return memberRepository.getByUserId(user.getId()).stream().filter(i -> i.getGroupId() == ids.get(0)).findFirst().get().getRoles().contains(MemberRole.ADMIN);
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
            Member member = memberRepository.getById(id);
            if (member == null)
                continue;
            List<Member> memberList = memberRepository.getByGroupId(member.getGroupId());
            if (!checkUtil.checkUserForMemberRole(memberList, user, MemberRole.ADMIN)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkUserForMember() throws Exception {
        for (Long id : ids) {
            Member member = memberRepository.getById(id);
            if (member == null)
                continue;
            List<Member> memberList = memberRepository.getByGroupId(member.getGroupId());
            if (!checkUtil.checkUserForMemberRole(memberList, user, MemberRole.MEMBER)) {
                return false;
            }
        }
        return true;
    }
}
