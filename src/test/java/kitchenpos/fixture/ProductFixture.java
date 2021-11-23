package kitchenpos.fixture;

import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.ProductRequest;
import kitchenpos.ui.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductFixture {

    private final ProductRepository productRepository;

    public ProductFixture(ProductRepository productRepository) {
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


}
