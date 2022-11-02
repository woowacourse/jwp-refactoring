package kitchenpos.menu.ui.request;

import java.math.BigDecimal;

import kitchenpos.menu.application.request.ProductRequest;

public class ProductApiRequest {

    private final String name;
    private final BigDecimal price;

    public ProductApiRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public ProductRequest toServiceRequest() {
        return new ProductRequest(name, price);
    }
}
