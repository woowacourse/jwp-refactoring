package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private String productName;
    private Price productPrice;
    private Long quantity;

    public MenuProduct(Long menuId, String productName, BigDecimal productPrice, Long quantity) {
        this.menuId = menuId;
        this.productName = productName;
        this.productPrice = new Price(productPrice);
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Long menuId, String productName, BigDecimal productPrice, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productName = productName;
        this.productPrice = new Price(productPrice);
        this.quantity = quantity;
    }

    public MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getProductPrice() {
        return productPrice.getValue().longValue();
    }

    public Long getQuantity() {
        return quantity;
    }
}
