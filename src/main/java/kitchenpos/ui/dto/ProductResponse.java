package kitchenpos.ui.dto;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final Price price;

    public ProductResponse(final Long id, final String name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
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
