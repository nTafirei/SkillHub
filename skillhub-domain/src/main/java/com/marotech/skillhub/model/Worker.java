package com.marotech.skillhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.lang3.StringUtils;


@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "worker")
public class Worker extends BaseEntity {

    @Column(length = 512)
    private String profile;
    @Column(nullable = false, length = 40)
    private String firstName;
    @Column(length = 128)
    private String email;
    @Column(nullable = false, length = 80)
    private String lastName;
    @Column(length = 16)
    private String title = "";
    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    private Attachment picture;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Showcase showcase = Showcase.NO;

    public Boolean getIsShowcased() {
        return showcase == Showcase.YES;
    }

    public boolean getHasPicture() {
        return picture != null;
    }

    public String getFullName() {
        if (StringUtils.isNoneBlank(title)) {
            return title + " " + firstName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
}
