package com.marotech.skillhub.model;

import com.marotech.skillhub.gson.GsonExcludeField;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "app_session")
public class AppSession extends BaseEntity {
    @Column(nullable = false)
    private String mobileNumber;
    @Column(nullable = false)
    private int otp = 0;
    @Column(nullable = false)
    private int otpTTL = 0;
    @Column(nullable = false)
    private String token = UUID.randomUUID().toString();
}