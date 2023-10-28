package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private long quantity;

    private MenuProductResponse(
            Long productId,
            String productName,
            BigDecimal productPrice,
            long quantity
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getProductId(),
                menuProduct.getProductName(),
                menuProduct.getProductPrice(),
                menuProduct.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
