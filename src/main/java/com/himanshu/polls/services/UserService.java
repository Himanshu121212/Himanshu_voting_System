/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.himanshu.polls.services;

import com.himanshu.polls.entity.Role;
import com.himanshu.polls.entity.RoleEnum;
import com.himanshu.polls.entity.User;
import com.himanshu.polls.repository.RoleRepository;
import com.himanshu.polls.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User saveUser(User user) {

        User existingUser = userRepository.findOneByUsername(user.getUsername());

        Role userRole = new Role(RoleEnum.ROLE_USER.toString());
        roleRepository.save(userRole);

        String password = bcryptEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRoles(Arrays.asList(userRole));
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists exception");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers(boolean includeAdmins) {
        if (includeAdmins) {
            return userRepository.findAll();
        } else {
            return userRepository.findAll().stream()
                .filter(
                    u -> u.getRoles().stream()
                        .noneMatch(role -> role.getRole().equals(RoleEnum.ROLE_ADMIN.name()))
                ).collect(Collectors.toList());
        }
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
