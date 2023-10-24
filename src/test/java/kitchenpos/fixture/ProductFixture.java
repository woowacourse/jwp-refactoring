package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_망고_1000원() {
        return new Product("망고", BigDecimal.valueOf(1000));
    }

    public static Product 상품_망고_N원(final int price) {
        return new Product("망고", BigDecimal.valueOf(price));
    }

    public static Product 상품_치킨_15000원() {
        return new Product("치킨", BigDecimal.valueOf(15000));
    }

    public static Product 상품_존재X() {
        return new Product(999999L, "INVALID", BigDecimal.valueOf(999999));
    }

    public static ProductCreateRequest 상품요청_생성(final Product product) {
        return new ProductCreateRequest(product.getName(), product.getPrice());
    }

    public static ProductCreateRequest 상품요청_생성(final String name, final int price) {
        return new ProductCreateRequest(name, BigDecimal.valueOf(price));
    }
}
