package kitchenpos.product.ui.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private Long price;

    private ProductResponse() {
    }

    private ProductResponse(final Long id, final String name, final Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
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

    public Long getPrice() {
        return price;
    }
}
