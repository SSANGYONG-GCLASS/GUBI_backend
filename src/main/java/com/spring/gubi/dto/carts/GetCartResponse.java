package com.spring.gubi.dto.carts;

import com.spring.gubi.domain.carts.Cart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCartResponse {
    private Long cartNo;
    private Integer cartCnt; // 수량

    // 상품
    private Long productNo;
    private String productName;
    private String productThumbnailImg;
    private Integer productPrice;
    private Integer productDeliveryPrice;
    private Integer ProductPointPct;

    // 상품 옵션
    private Long optionNo;
    private String optionName;
    private String optionColor;
    private String optionImg;

    public GetCartResponse(Cart cart) {
        this.cartNo = cart.getId();
        this.cartCnt = cart.getCnt();

        this.productNo = cart.getOption().getProduct().getId();
        this.productName = cart.getOption().getProduct().getName();
        this.productThumbnailImg = cart.getOption().getProduct().getThumbnail_img();
        this.productPrice = cart.getOption().getProduct().getPrice();
        this.productDeliveryPrice = cart.getOption().getProduct().getDelivery_price();
        this.ProductPointPct = cart.getOption().getProduct().getPoint_pct();

        this.optionNo = cart.getOption().getId();
        this.optionName = cart.getOption().getName();
        this.optionColor = cart.getOption().getColor();
        this.optionImg = cart.getOption().getImg();
    }
}
