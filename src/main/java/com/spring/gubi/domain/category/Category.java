package com.spring.gubi.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Category {
	
	@Id
	@Column(name = "categoryno", nullable = false, unique = true, updatable = false)
	@SequenceGenerator(name = "SEQ_CATEGORY_GENERATOR", sequenceName = "category_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CATEGORY_GENERATOR")
	private Long id;
	
	@Column(name = "major_category", nullable = false)
	private String major_category;
	
	@Column(name = "small_category", nullable = false)
	private String small_category;
	
	@Column(name = "category_img", nullable = false)
	private String category_img;
}
