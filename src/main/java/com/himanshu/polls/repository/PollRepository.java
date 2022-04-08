/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.himanshu.polls.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.himanshu.polls.entity.Poll;
import com.himanshu.polls.entity.User;


public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findAllByUser(User user);

    public List<Poll> findAllByUserAndVisible(User user, boolean b);

}
