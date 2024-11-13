package ru.kata.spring.boot_security.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;

   @Column(name = "name")
   @NotEmpty(message = "Enter name!")
   private String firstName;

   @Column(name = "last_name")
   @NotEmpty(message = "Enter last name!")
   private String lastName;

   @Column(name = "password")
   @NotEmpty(message = "Enter password!")
   private String password;

   @Column(name = "email")
   @NotEmpty(message = "Enter email!")
   private String email;

   @Column(name = "age")
   @NotEmpty(message = "Enter age!")
   private int age;

   @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
   @NotEmpty(message = "Select at least 1 role")
   @JsonManagedReference
   @JoinTable(
         name = "users_roles",
         joinColumns = @JoinColumn(name = "user_id"),
         inverseJoinColumns = @JoinColumn(name = "role_id"))
   private List<Role> roles;

   public User() {}

   public User(String firstName, String lastName, int age, String password, String email, List<Role> roles) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.age = age;
      this.password = password;
      this.email = email;
      this.roles = roles;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public List<Role> getRoles() {
      return roles;
   }

   public void setRoles(List<Role> roles) {
      this.roles = roles;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }
}
