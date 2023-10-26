package kitchenpos.fixture;

import kitchenpos.domain.product.Product;
import kitchenpos.support.money.Money;

public class ProductFixture {

    public static Product 상품(String name, Long price) {
        return new Product(name, Money.valueOf(price));
    }

    public static Product 상품(Long id, String name, Long price) {
        return new Product(id, name, Money.valueOf(price));
    }
}
