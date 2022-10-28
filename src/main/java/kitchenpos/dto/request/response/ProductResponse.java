package kitchenpos.dto.request.response;

import kitchenpos.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;

    private ProductResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
