package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final Integer price;

    public ProductResponse(Product product) {
        this(
            product.getId(),
            product.getName(),
            product.getPriceAsInteger()
        );
    }

    public ProductResponse(Long id, String name, Integer price) {
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

    public Integer getPrice() {
        return price;
    }
}
