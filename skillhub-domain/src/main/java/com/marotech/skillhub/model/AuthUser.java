package com.marotech.skillhub.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;


@ToString
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

    public static final String encodedPassword(String original) throws Exception {
        return DigestUtils.sha256Hex(original);
    }
}
