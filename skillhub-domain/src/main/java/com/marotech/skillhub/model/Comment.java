package com.marotech.skillhub.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
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
	@OneToOne(fetch = FetchType.EAGER)
	private Comment parentNote;
	@Column
	private LocalDateTime archivedDate;
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private User talent;
}