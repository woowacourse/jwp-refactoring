package kitchenpos.fixture;

import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_생성_10000원() {
        return new Product(null, "상품", 10000L);
    }

    public static ProductCreateRequest 상품_생성_요청(final Product product) {
        return new ProductCreateRequest(product.getName(), product.getPrice());
    }
}
