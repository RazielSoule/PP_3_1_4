package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotAddedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserNotUpdatedException;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ApiController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/get-all-users")
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/get-all-roles")
    public List<Role> getAllRoles() {
        return roleService.getRoles();
    }

    @GetMapping(value = "/get-user")
    public User getUser(@RequestParam(value = "id") int id) {
        return userService.getUser(id);
    }

    @GetMapping(value = "/get-user-form")
    public User getUserForm() {
        return new User();
    }

    @PostMapping(value = "/add-user")
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid User user,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new UserNotAddedException(errorStringBuild(bindingResult));
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/edit-user")
    public ResponseEntity<HttpStatus> editUser(@RequestBody @Valid User user,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new UserNotUpdatedException(errorStringBuild(bindingResult));
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/delete-user")
    public HttpStatus deleteUser(@RequestParam(value = "id") int id) {
        userService.deleteUser(id);
        return HttpStatus.OK;
    }

    private String errorStringBuild(BindingResult bindingResult) {
        StringBuilder errors = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.append(error.getField())
                    .append(": ").append(error.getDefaultMessage())
                    .append(";\n");
        }
        return errors.toString();
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException ex) {
        UserErrorResponse response = new UserErrorResponse(
                "User not found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotAddedException ex) {
        UserErrorResponse response = new UserErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
