package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private int price;

    private ProductCreateRequest() {
    }

    public Product toProduct() {
        return new Product(null, name, new BigDecimal(price));
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
