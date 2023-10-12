package kitchenpos.fixture;

import kitchenpos.application.product.dto.ProductCreateRequest;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품_생성_10000원() {
        return new Product("상품", new BigDecimal(10000));
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        return new Product(name, price);
    }

    public static ProductCreateRequest 상품_생성_요청(final Product product) {
        return new ProductCreateRequest(product.getName(), product.getPrice());
    }
}
