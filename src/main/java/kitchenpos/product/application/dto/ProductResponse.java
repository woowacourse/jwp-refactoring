package kitchenpos.product.application.dto;

import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private long price;

    private ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice().longValue()
        );
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
