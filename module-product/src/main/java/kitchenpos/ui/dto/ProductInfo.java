package kitchenpos.ui.dto;

import kitchenpos.domain.Product;

public class ProductInfo {

    private String name;
    private Long price;

    public ProductInfo(String name, Long price) {
        this.name = name;
        this.price = price;
    }

    public static ProductInfo from(Product product) {
        return new ProductInfo(
                product.name(),
                product.id()
        );
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }
}
