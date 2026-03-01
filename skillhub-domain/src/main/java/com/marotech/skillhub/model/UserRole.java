package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user_role")
public class UserRole extends BaseEntity {

    @Column(nullable = false,length = 32)
    private String roleName;
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "userRoles", targetEntity = User.class)
    private List<User> users;

}
