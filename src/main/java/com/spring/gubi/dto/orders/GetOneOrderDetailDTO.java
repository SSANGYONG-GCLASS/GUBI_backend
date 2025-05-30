package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.OrderDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOneOrderDetailDTO {
    private Long id;
    private Integer cnt; // 수량
    private Integer price; // 단가

    private Long productNo;
    private String productName;

    private Long optionNo; // 옵션 번호 (FK)
    private String optionName;
    private String optionColor;
    private String optionImg;

    public GetOneOrderDetailDTO(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.cnt = orderDetail.getCnt();
        this.price = orderDetail.getPrice();

        this.productNo = orderDetail.getOption().getProduct().getId();
        this.productName = orderDetail.getOption().getProduct().getName();

        this.optionNo = orderDetail.getOption().getId();
        this.optionName = orderDetail.getOption().getName();
        this.optionColor = orderDetail.getOption().getColor();
        this.optionImg = orderDetail.getOption().getImg();
    }
}
