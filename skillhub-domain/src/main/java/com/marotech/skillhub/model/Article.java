package com.marotech.skillhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "article")
public class Article extends BaseEntity {

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String summary;
    @Column(nullable = false)
    private String fileName;
    @Enumerated(EnumType.STRING)
    @NotNull
    private PubType pubType = PubType.SELF_REVIEW;
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Skill skill;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Showcase showcase = Showcase.NO;
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    private List<Attachment> attachments;
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private User talent;

    public Boolean getIsShowcased() {
        return showcase == Showcase.YES;
    }

    public String getFormattedPubDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return fmt.format(dateCreated);
    }
}
