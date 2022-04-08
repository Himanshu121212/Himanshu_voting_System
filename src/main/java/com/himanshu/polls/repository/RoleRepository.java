/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.himanshu.polls.repository;

import org.springframework.data.repository.CrudRepository;

import com.himanshu.polls.entity.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {
    
}
