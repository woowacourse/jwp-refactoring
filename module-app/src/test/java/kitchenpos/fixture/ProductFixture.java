package kitchenpos.fixture;

import application.dto.ProductCreateRequest;
import application.dto.ProductResponse;
import domain.Product;
import domain.ProductName;
import java.math.BigDecimal;
import vo.Price;

public class ProductFixture {

    public static Product 상품(
            final String name,
            final BigDecimal price
    ) {
        final ProductName productName = new ProductName(name);
        final Price productPrice = new Price(price);
        return new Product(productName, productPrice);
    }

    public static ProductCreateRequest 상품_등록_요청(final String name, final BigDecimal price) {
        return new ProductCreateRequest(name, price);
    }

    public static ProductResponse 상품_등록_응답(final Product product) {
        return ProductResponse.from(product);
    }
}
