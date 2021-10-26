package kitchenpos.application.dtos;

import kitchenpos.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final Long price;

    public ProductResponse(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice().longValue();
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
