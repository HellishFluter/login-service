package ru.weber.login.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{

    /**Имя*/
    @NotNull
    @Column(name = "first_name")
    private String firstName;

    /**Фамилия*/
    @NotNull
    @Column(name = "last_name")
    private String lastName;

    /**Отчетсво*/
    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "login")
    private String login;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}
