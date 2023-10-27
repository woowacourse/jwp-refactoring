package kitchenpos.product.request;

import java.math.BigDecimal;

public class ProductCreateRequest {
    private String name;
    private BigDecimal price;

    public ProductCreateRequest(String name, Long price) {
        this.name = name;
        this.price = toBigDecimal(price);
    }

    private BigDecimal toBigDecimal(Long price) {
        if (price == null) {
            return null;
        }
        return BigDecimal.valueOf(price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
