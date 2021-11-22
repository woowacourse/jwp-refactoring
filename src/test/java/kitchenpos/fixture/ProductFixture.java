package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductRequest;

public class ProductFixture {

    public ProductRequest 상품_생성_요청(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public Product 상품_생성(Long id, String name, BigDecimal price) {
        return Product.create(id, name, price);
    }

    public List<Product> 상품_리스트_생성(Product... products) {
        return Arrays.asList(products);
    }
}
