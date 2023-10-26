package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final long price;

    private ProductResponse(final Long id, final String name, final long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName().getName(), product.getPrice().getPrice().longValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
