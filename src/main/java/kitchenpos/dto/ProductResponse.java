package kitchenpos.dto;

import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;

    private String name;

    private Integer price;

    public ProductResponse(final Long id, final String name, final Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductResponse(final Product product) {
        this(product.getId(), product.getName(), product.getPrice().intValue());
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
