package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

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
        return new Product(new Name(this.name), Price.valueOf(this.price));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
