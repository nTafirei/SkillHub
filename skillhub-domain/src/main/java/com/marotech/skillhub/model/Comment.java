package com.marotech.skillhub.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {
	@Column(nullable = false)
	@NotNull
	private String title;
	@Column(length = 512, nullable = false)
	@NotNull
	private String body;
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private User createdBy;
	@ManyToOne(fetch = FetchType.EAGER)
	private Comment parentNode;
	@Column
	private LocalDateTime archivedDate;
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private User talent;
	@Enumerated(EnumType.STRING)
	private PubType pubType = PubType.THIRD_PARTY_REVIEW;
	@Transient
	private List<Comment> children = new ArrayList<>();
}