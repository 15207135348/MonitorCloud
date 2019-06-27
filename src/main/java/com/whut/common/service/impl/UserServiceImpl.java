package com.whut.common.service.impl;

import com.whut.common.dao.mysql.UserRepository;
import com.whut.common.entity.User;
import com.whut.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yy
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {

        userRepository.save(user);

    }
    @Override
    public void deleteUser(String userName) {
        userRepository.deleteUserByUsername(userName);
    }

    @Override
    public void updateUser(String userName,User user) {

        User user1=userRepository.findUserByUsername(userName);
        if(user1!=null) {
            user.setId(user1.getId());
        }
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findById(int id) {
        return userRepository.findUserById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}