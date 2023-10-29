package kitchenpos.core.product.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.core.product.domain.Name;
import kitchenpos.core.product.domain.Price;
import kitchenpos.core.product.domain.Product;

public class ProductResponse {
    @JsonProperty("id")
    private final Long id;

    @JsonUnwrapped
    private final Name name;

    @JsonUnwrapped
    private final Price price;

    public ProductResponse(final Long id, final Name name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static List<ProductResponse> from(final List<Product> products) {
        return products
                .stream().map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
