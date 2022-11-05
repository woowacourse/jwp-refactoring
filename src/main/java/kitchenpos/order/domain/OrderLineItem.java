package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private String menuName;
    private Price menuPrice;
    private Long menuQuantity;
    private String productName;
    private Price productPrice;
    private Long productQuantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq,
                         Long orderId,
                         Long menuId,
                         String menuName,
                         BigDecimal menuPrice,
                         Long menuQuantity,
                         String productName,
                         BigDecimal productPrice,
                         Long productQuantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = new Price(menuPrice);
        this.menuQuantity = menuQuantity;
        this.productName = productName;
        this.productPrice = new Price(productPrice);
        this.productQuantity = productQuantity;
    }

    public OrderLineItem(Long menuId, String menuName, BigDecimal price, Long quantity, String productName,
                         BigDecimal productPrice, Long productQuantity) {
        this(null, null, menuId, menuName, price, quantity, productName, productPrice, productQuantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getValue();
    }

    public Long getMenuQuantity() {
        return menuQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice.getValue();
    }

    public Long getProductQuantity() {
        return productQuantity;
    }
}
