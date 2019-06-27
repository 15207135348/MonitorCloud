package com.whut.common.dao.mysql;

import com.whut.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by YY on 2018-03-26.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findUserById(int id);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    void deleteUserByUsername(String username);
}
