package com.kitchenpos.fixture;

import com.kitchenpos.application.dto.ProductCreateRequest;
import com.kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_생성_10000원() {
        return new Product(null, "상품", 10000L);
    }

    public static ProductCreateRequest 상품_생성_요청(final Product product) {
        return new ProductCreateRequest(product.getName(), product.getPrice());
    }
}
