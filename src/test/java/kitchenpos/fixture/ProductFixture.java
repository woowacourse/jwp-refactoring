package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductName;
import kitchenpos.domain.product.ProductPrice;

public class ProductFixture {

    public static Product 상품(
            final String name,
            final BigDecimal price
    ) {
        final ProductName productName = new ProductName(name);
        final ProductPrice productPrice = new ProductPrice(price);
        return new Product(productName, productPrice);
    }

    public static ProductCreateRequest 상품_등록_요청(final String name, final BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }

    public static ProductResponse 상품_등록_응답(final Product product) {
        return ProductResponse.from(product);
    }
}
