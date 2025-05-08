package com.spring.gubi.domain.product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Option {
	
	@Id
	@Column(name = "optionno", nullable = false, unique = true, updatable = false)
	@SequenceGenerator(name = "SEQ_OPTION_GENERATOR", sequenceName = "option_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OPTION_GENERATOR")
	private Long id;
	
	@JoinColumn(name = "fk_productno", referencedColumnName = "productno", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Product product;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "color", nullable = false)
	private String color;
	
	@Column(name = "img", nullable = false)
	private String img;
	
	
}
