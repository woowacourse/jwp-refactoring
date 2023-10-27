package kitchenpos.application.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import kitchenpos.domain.menu.Product;

public class CreateProductResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("price")
    private final BigDecimal price;

    private CreateProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static CreateProductResponse from(Product product) {
        return new CreateProductResponse(product.id(), product.name(), product.price().value());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }
}
