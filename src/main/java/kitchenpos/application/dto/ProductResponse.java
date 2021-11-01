package kitchenpos.application.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Price price;

    public ProductResponse(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
