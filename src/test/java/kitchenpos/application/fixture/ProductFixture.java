package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후라이드_치킨() {
        Product chicken = new Product();
        chicken.setName("후라이드치킨");
        chicken.setPrice(BigDecimal.valueOf(18000));

        return chicken;
    }

    public static Product 양념_치킨() {
        Product pizza = new Product();
        pizza.setName("양념치킨");
        pizza.setPrice(BigDecimal.valueOf(19000));

        return pizza;
    }

    public static Product 포테이토_피자() {
        Product pizza = new Product();
        pizza.setName("포테이토피자");
        pizza.setPrice(BigDecimal.valueOf(15000));

        return pizza;
    }
}
