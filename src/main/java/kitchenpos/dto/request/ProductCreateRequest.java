package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.product.Product;

public class ProductCreateRequest {

    @NotNull
    private String name;

    @NotNull
    private Integer price;

    private ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name, final Integer price) {
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(this.name, this.price);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
