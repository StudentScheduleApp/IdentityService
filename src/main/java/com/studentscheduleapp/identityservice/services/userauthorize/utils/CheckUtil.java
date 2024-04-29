package com.studentscheduleapp.identityservice.services.userauthorize.utils;

import com.studentscheduleapp.identityservice.models.Member;
import com.studentscheduleapp.identityservice.models.MemberRole;
import com.studentscheduleapp.identityservice.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckUtil {
    public boolean checkUserForMemberRole(List<Member> memberList, User user, MemberRole role) {
        for (Member member : memberList) {
            if (member.getUserId() == user.getId()) {
                if (member.getRoles().contains(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
