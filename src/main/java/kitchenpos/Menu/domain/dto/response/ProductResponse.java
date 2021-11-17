package kitchenpos.Menu.domain.dto.response;

import kitchenpos.Menu.domain.Product;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    protected ProductResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static ProductResponse toDTO(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
