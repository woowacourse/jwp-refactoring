package kitchenpos.dto.response;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse(Product product) {
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
    }
}
