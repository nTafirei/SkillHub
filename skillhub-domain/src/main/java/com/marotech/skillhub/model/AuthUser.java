package com.marotech.skillhub.model;

import com.marotech.skillhub.gson.GsonExcludeField;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;


@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "auth_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"})})
public class AuthUser extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String userName;
    @Column(length = 128)
    private String password;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER, cascade = {jakarta.persistence.CascadeType.REMOVE})
    private User user;
    @ToString.Exclude
    @GsonExcludeField
    @OneToMany(fetch = FetchType.EAGER)
    private List<SecurityQuestion> securityQuestions = new ArrayList<>();
    public static final String encodedPassword(String original) throws Exception {
        return DigestUtils.sha256Hex(original);
    }
}
