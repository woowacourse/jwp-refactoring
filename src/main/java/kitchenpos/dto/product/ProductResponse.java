package kitchenpos.dto.product;

import kitchenpos.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final int price;

    private ProductResponse(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product savedProduct) {
        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice().intValue()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
