package kitchenpos.product.application.dto.response;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductQueryResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductQueryResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductQueryResponse() {
    }

    public static ProductQueryResponse from(final Product product) {
        return new ProductQueryResponse(product.getId(), product.getName(),
                product.getPrice().getValue());
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

}
