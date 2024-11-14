package ru.kata.spring.boot_security.demo.service;

import jakarta.validation.constraints.Null;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
       this.passwordEncoder = passwordEncoder;
   }

   @Transactional
   @Override
   public void addUser(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
   }

   @Override
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public List<User> getUsers() {
      return userRepository.findAll();
   }

   @Override
   public User getUser(long id) {
      return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
   }

   @Override
   public User getUserByUsername(String username) {
      return userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
   }

   @Transactional
   @Override
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public void deleteUser(long id) {
      userRepository.deleteById(id);
   }

   @Transactional
   @Override
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public void updateUser(User user) {
      if (!user.getPassword().equals(userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new).getPassword()))
         user.setPassword(passwordEncoder.encode(user.getPassword()));
      if (user.getRoles() == null)
         user.setRoles(userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new).getRoles());
      userRepository.save(user);
   }
}
