package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.common.domain.Money;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static ProductResponse from(Product product) {
        Money price = product.getPrice();
        return new ProductResponse(product.getId(), product.getName(), price.getAmount());
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
