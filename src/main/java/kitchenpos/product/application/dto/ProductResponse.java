package kitchenpos.product.application.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final Integer price;

    public ProductResponse(Long id, Name name, Price price) {
        this.id = id;
        this.name = name.getValue();
        this.price = price.getValue().intValue();
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
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
