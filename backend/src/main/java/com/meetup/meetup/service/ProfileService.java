package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public User getUserByLogin(String login) {
        return userDao.findByLogin(login);
    }

    public User updateUser(User newUser) {
        return userDao.update(newUser);
    }

    public List<User> getFriends() {
        User user = authenticationFacade.getAuthentication();
        return userDao.getFriends(user.getId());
    }

    public List<User> getFriendsRequests() {
        User user = authenticationFacade.getAuthentication();
        return userDao.getFriendsRequests(user.getId());
    }

    public boolean addFriend(int friendId){
        User user = authenticationFacade.getAuthentication();
        User friend = userDao.findById(friendId);
        return friend != null && userDao.addFriend(user.getId(), friend.getId());
    }

    public void confirmFriend(int friendId){
        User user = authenticationFacade.getAuthentication();
        userDao.confirmFriend(user.getId(), friendId);
    }

    public void deleteFriend(int friendId){
        User user = authenticationFacade.getAuthentication();
        userDao.deleteFriend(user.getId(), friendId);
    }
}