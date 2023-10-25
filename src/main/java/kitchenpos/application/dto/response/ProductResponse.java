package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.domain.product.Product;

public class ProductResponse {

    private final Long id;
    private final String productName;
    private final BigDecimal productPrice;

    private ProductResponse(final Long id, final String productName, final BigDecimal productPrice) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductName().getName(),
                product.getProductPrice().getPrice()
        );
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }
}
