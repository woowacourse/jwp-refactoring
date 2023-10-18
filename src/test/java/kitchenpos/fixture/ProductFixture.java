package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.refactoring.domain.Price;
import kitchenpos.refactoring.domain.Product;

public class ProductFixture {

    public static Product create(String name, long price) {
        BigDecimal bigDecimal = BigDecimal.valueOf(price);

        return new Product(name, new Price(bigDecimal));
    }

}
