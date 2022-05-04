package com.nazarov.tinkoffcontest.service;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return  user;
    }

    public boolean addUser(User user) {

        User userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb != null) {
            return false;
        }
        LOGGER.info("Usr created: name {}, email {}, pass {}", user.getUsername(), user.getEmail(), user.getPassword());

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to demo. Activation code:\n%s",
                    user.getUsername(), user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if(user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }



    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for(String key : form.keySet()) {
            if(roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public boolean isMailApproved(User user) {

        User currentUser = userRepository.findByUsername(user.getUsername());
        if(currentUser.getActivationCode() != null) {
            return false;
        } else {
            return true;
        }
    }

    public void changeActive(User user, boolean isActive) {

        if(isActive) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }
        userRepository.save(user);
    }
    @Transactional
    public void changeRole(User user, Set<Role> role) {
        if(role.contains(Role.ADMIN)) {
            role.remove(Role.ADMIN);
            userRepository.deleteAdmin(user.getId());
        } else {
            role.add(Role.ADMIN);
        }
        user.setRoles(role);
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Long countAll() {
        return userRepository.count();
    }

    public int countAllAdmins() {
        return  userRepository.findAdmins();
    }

    public int countAllUsers() {
        return userRepository.findUsers();
    }

    public int activeAccounts() { return  userRepository.activeAccounts(); }

    public int inactiveAccounts() { return  userRepository.inactiveAccounts(); }

    public List<User> mainSelectActive(String username, String email) {
        return userRepository.mainSelectActive(username, email);
    }

    public List<User> mainSelectInactive(String username, String email) {
        return userRepository.mainSelectInactive(username, email);
    }
}