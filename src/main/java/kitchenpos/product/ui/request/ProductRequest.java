package kitchenpos.product.ui.request;

import java.math.BigDecimal;
import kitchenpos.product.application.request.ProductCommand;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    private ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductCommand newProductCommand() {
        return new ProductCommand(name, price);
    }
}
