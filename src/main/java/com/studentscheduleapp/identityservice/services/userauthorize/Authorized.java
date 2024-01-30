package com.studentscheduleapp.identityservice.services.userauthorize;

import com.studentscheduleapp.identityservice.models.AuthorizeType;
import com.studentscheduleapp.identityservice.models.Entity;

import java.util.List;
import java.util.Set;

public abstract class Authorized {
    private AuthorizeType type;
    private List<Long> ids;
    private Set<String> params;

    public Authorized(AuthorizeType type, List<Long> ids, Set<String> params) {
        this.type = type;
        this.ids = ids;
        this.params = params;
    }

    public final boolean authorize() {
        switch (type){
            case GET:
                return authorizeGet();
            case CREATE:
                return authorizeCreate();
            case PATCH:
                return authorizePatch();
            case DELETE:
                return authorizeDelete();
        }
        return false;
    }

    protected abstract boolean authorizeDelete();

    protected abstract boolean authorizePatch();

    protected abstract boolean authorizeCreate();

    protected abstract boolean authorizeGet();
}

