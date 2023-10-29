package kitchenpos.product.service.dto;

import kitchenpos.product.Product;

public class ProductResponse {

    private final Long id;

    private final String name;

    private final Long price;

    public ProductResponse(final Long id, final String name, final Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse toDto(final Product product) {
        final long price = product.getPrice().getPrice().longValue();
        return new ProductResponse(product.getId(), product.getName(), price);
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
