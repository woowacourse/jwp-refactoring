package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.dao.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductServiceTest {

    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void 상품_생성할_수_있다() {
        ProductCreateRequest request = new ProductCreateRequest("로제떡볶이", BigDecimal.valueOf(1000,2));

        ProductResponse productResponse = productService.create(request);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(request.getName()).isEqualTo(productResponse.getName());
            softAssertions.assertThat(request.getPrice()).isEqualTo(productResponse.getPrice());
        });
    }

    @Test
    void 전체_상품_조회할_수_있다() {
        List<ProductResponse> responses = productService.list();
        Assertions.assertThat(responses.size()).isGreaterThan(0);
    }
}
