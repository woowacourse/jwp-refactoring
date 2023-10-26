package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class CreateProductResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    public CreateProductResponse() {
    }

    public CreateProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static CreateProductResponse from(final Product product) {
        return new CreateProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
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
