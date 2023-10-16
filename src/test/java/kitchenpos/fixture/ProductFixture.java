package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 아메리카노(){
        return new Product("아메리카노", BigDecimal.valueOf(5600));
    }
}
