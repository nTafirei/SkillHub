package com.marotech.skillhub.model;
import jakarta.persistence.*;
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
	@Column
	private String title;
	@Column(length = 512)
	private String body;
	@ManyToOne(fetch = FetchType.EAGER)
	private User createdBy;
	@OneToOne(fetch = FetchType.EAGER)
	private Comment parentNote;
	@Column
	private LocalDateTime archivedDate;
}