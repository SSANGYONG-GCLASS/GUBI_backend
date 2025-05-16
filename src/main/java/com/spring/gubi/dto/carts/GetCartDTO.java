package com.spring.gubi.dto.carts;

import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Getter
@Setter
public class GetCartDTO {
    private Long cartNo;
    private Integer cartCnt; // 수량

    // 상품
    private Long productNo;
    private String productName;
    private String productThumbnailImg;
    private Integer productPrice;
    private Integer productDeliveryPrice;
    private Integer productPoint;

    // 상품 옵션
    private Long optionNo;
    private String optionName;
    private String optionColor;
    private String optionImg;
    private Integer optionCnt;
    private Boolean optionInStock;

    public GetCartDTO(Cart cart) {
        this.cartNo = cart.getId();
        this.cartCnt = cart.getCnt();

        Product product = cart.getOption().getProduct();
        this.productNo = product.getId();
        this.productName = product.getName();
        this.productThumbnailImg = product.getThumbnail_img();
        this.productPrice = product.getPrice();
        this.productDeliveryPrice = product.getDelivery_price();
        this.productPoint = BigDecimal.valueOf(product.getPrice())
                .multiply(product.getPoint_pct())
                .intValue();

        this.optionNo = cart.getOption().getId();
        this.optionName = cart.getOption().getName();
        this.optionColor = cart.getOption().getColor();
        this.optionImg = cart.getOption().getImg();
        this.optionCnt = cart.getOption().getCnt();
        this.optionInStock = cart.getOption().getCnt() > 0;
    }
}
