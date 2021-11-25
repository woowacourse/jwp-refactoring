package kitchenpos.product.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.DefaultFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.ui.dto.request.ProductRequest;
import kitchenpos.product.ui.dto.response.ProductResponse;
import kitchenpos.testtool.RequestBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class ProductFixture extends DefaultFixture {

    private final ProductRepository productRepository;

    public ProductFixture(RequestBuilder requestBuilder, ProductRepository productRepository) {
        super(requestBuilder);
        this.productRepository = productRepository;
    }

    public ProductRequest 상품_생성_요청(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public Product 상품_조회(Long id) {
        return productRepository.getOne(id);
    }

    public List<ProductResponse> 상품_리스트_생성(ProductResponse... productResponses) {
        return Arrays.asList(productResponses);
    }

    public ProductResponse 상품_등록(ProductRequest request) {
        return request()
                .post("/api/products", request)
                .build()
                .convertBody(ProductResponse.class);
    }

    public List<ProductResponse> 상품_리스트_조회() {
        return request()
                .get("/api/products")
                .build()
                .convertBodyToList(ProductResponse.class);
    }
}
