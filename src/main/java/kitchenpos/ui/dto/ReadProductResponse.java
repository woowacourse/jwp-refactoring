package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ReadProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    public ReadProductResponse(final Long id, final String name, final BigDecimal price) {
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

    public static ReadProductResponse from(final Product product) {
        return new ReadProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
