package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.models.Role;
import com.studentscheduleapp.identityservice.repos.*;
import com.studentscheduleapp.identityservice.security.JwtProvider;
import com.studentscheduleapp.identityservice.services.userauthorize.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MemberAuthorizeService extends Authorized {
    @Autowired
    private MemberRepository memberRepository;
    private CheckUtil checkUtil;

    public MemberAuthorizeService(UserRepository userRepository, JwtProvider jwtProvider) {
        super(userRepository, jwtProvider);
    }

    @Override
    protected boolean authorizeDelete() {
        try {
            List<Member> members = memberRepository.getByUserId(user.getId());
            for(Long id : ids){
                Member member = memberRepository.getById(id);
                if (member.getRoles().contains(MemberRole.OWNER))
                    return false;
                Member m = members.stream().filter( i -> i.getGroupId() == member.getGroupId()).findFirst().get();
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
            for(Long id : ids){
                Member member = memberRepository.getById(id);
                if (member.getRoles().contains(MemberRole.OWNER))
                    return false;
                Member m = members.stream().filter( i -> i.getGroupId() == member.getGroupId()).findFirst().get();
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
            return checkUserForAdmin();
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
        List<Member> members = new ArrayList<>();
        for(Long id : ids){
            members.add(memberRepository.getById(id));
        }
        if(!checkUtil.checkUserForMemberRole(members,user, MemberRole.ADMIN)){
            return false;
        }
        return true;
    }
    private boolean checkUserForMember() throws Exception {
        List<Member> members = new ArrayList<>();
        for(Long id : ids){
            members.add(memberRepository.getById(id));
        }
        if(!checkUtil.checkUserForMemberRole(members,user, MemberRole.MEMBER)){
            return false;
        }
        return true;
    }
}
