package com.whut.common.service;

import com.whut.common.entity.User;



/**
 * @author YY
 */
public interface UserService {

    void addUser(User user);
    void deleteUser(String userName);
    void updateUser(String username, User user);
    User findByUsername(String username);
    User findById(int id);
    User findByEmail(String email);
}
