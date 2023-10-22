package kitchenpos.application.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class SearchProductResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("price")
    private final BigDecimal price;

    private SearchProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static SearchProductResponse from(Product product) {
        return new SearchProductResponse(product.id(), product.name(), product.price());
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
