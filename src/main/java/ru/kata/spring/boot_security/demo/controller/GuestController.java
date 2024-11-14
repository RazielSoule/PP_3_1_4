package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@RestController
public class GuestController {

    private final UserServiceImp userServiceImp;
    private final RoleServiceImp roleServiceImp;
    private final UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    public GuestController(UserServiceImp userServiceImp, RoleServiceImp roleServiceImp, UserDetailsServiceImp userDetailsServiceImp) {
        this.userServiceImp = userServiceImp;
        this.roleServiceImp = roleServiceImp;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @GetMapping(value = "/createAdmin")
    public HttpStatus createAdmin() {
        if (userDetailsServiceImp.loadUserByUsername("admin@adm.in") == null) {
            userServiceImp.addUser(new User("ad", "min", 999, "admin", "admin@adm.in", roleServiceImp.getRoles()));
            return HttpStatus.OK;
        }
        return HttpStatus.FORBIDDEN;
    }
}
