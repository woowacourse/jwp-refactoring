package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품_생성(String name, BigDecimal price){
        return new Product(name, price);
    }
}
