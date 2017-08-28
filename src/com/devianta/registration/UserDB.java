package com.devianta.registration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDB implements Serializable {

    private static final long serialVersionUID = 1L;
    private Set<User> users = new HashSet<>();

    public UserDB() {
    }

    public synchronized boolean addUser(User user) {
        if (users.contains(user)) {
            return false;
        }

        for (User usr: users){
            if (user.getUsername().equals(usr.getUsername())) {
                return false;
            }
        }

        return users.add(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }
}
