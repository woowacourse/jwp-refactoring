package kitchenpos.application.dto.result;

import kitchenpos.domain.product.Product;

public class ProductResult {

    private final Long id;
    private final String name;
    private final Long price;

    public ProductResult(
            final Long id,
            final String name,
            final Long price
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResult from(final Product product) {
        return new ProductResult(
                product.getId(),
                product.getName(),
                product.getPrice().getValue().longValue()
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
