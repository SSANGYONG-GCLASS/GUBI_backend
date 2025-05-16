package com.spring.gubi.domain.product;

import java.time.LocalDateTime;

import com.spring.gubi.domain.category.Category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Product {
	
	@Id
	@Column(name = "productno", nullable = false, unique = true, updatable = false)
	@SequenceGenerator(name = "SEQ_PRODUCT_GENERATOR", sequenceName = "prod_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT_GENERATOR")
	private Long id;
	
	@JoinColumn(name = "fk_categoryno", referencedColumnName = "categoryno", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Category category;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "price", nullable = false)
	private Integer price;
	
	@Column(name = "thumbnail_img", nullable = false)
	private String thumbnail_img;
	
	@Column(name = "registerday", nullable = false)
	@Builder.Default
	private LocalDateTime registerday = LocalDateTime.now();
	
	@Column(name = "delivery_price", nullable = false)
	private Integer delivery_price;
	
	@Column(name = "detail_html", nullable = false)
	private String detail_html;
	
	@Column(name = "point_pct", nullable = false)
	private Integer point_pct;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	
	
}
