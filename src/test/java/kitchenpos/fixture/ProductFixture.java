package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Objects;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;

public class ProductFixture {

    public static final Long ID1 = 1L;
    public static final Long ID2 = 2L;
    public static final String NAME1 = "chicken";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(18000L);
    public static final BigDecimal NEGATIVE_PRICE = BigDecimal.valueOf(-18000L);

    public static Product createWithoutId() {
        return Product.of(NAME1, DEFAULT_PRICE);
    }

    public static Product createWithId(Long id) {
        return new Product(id, NAME1, DEFAULT_PRICE);
    }

    public static ProductCreateRequest createRequestPriceOf(Long price) {
        if (Objects.isNull(price)) {
            return new ProductCreateRequest(NAME1, null);
        }
        return new ProductCreateRequest(NAME1, BigDecimal.valueOf(price));
    }

    public static Product createNegativePriceWithId(Long id) {
        return new Product(id, NAME1, NEGATIVE_PRICE);
    }

    public static Product createNullPriceWithId(Long id) {
        return new Product(id, NAME1, null);
    }
}
