package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class ProductFixture {

    public static Product createChickenProductById(final Long id) {
        return new Product(id, "치킨", BigDecimal.valueOf(10000));
    }

    public static final Product CHICKEN_NON_ID = new Product(null, "치킨", BigDecimal.valueOf(10000));
    public static final Product CHICKEN = new Product(1L, "치킨", BigDecimal.valueOf(10000));
    public static final Product COKE_NON_ID = new Product(null, "콜라", BigDecimal.valueOf(1000));
    public static final Product COKE = new Product(2L, "콜라", BigDecimal.valueOf(1000));

    public static final ProductRequest CHICKEN_REQUEST = new ProductRequest("치킨", BigDecimal.valueOf(10000));
}
