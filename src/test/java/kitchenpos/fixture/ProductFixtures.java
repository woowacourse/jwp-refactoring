package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixtures {

    public static Product PIZZA() {
        final Product pizza = new Product();
        pizza.setName("피자");
        pizza.setPrice(BigDecimal.valueOf(20000));
        return pizza;
    }
}
