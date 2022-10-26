package kitchenpos.dto;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductCreateRequest {

    private String name;
    private int price;

    private ProductCreateRequest() {
    }

    public Product toProduct() {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        return product;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
