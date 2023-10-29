package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.application.request.ProductRequest;
import kitchenpos.product.application.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductServiceTest extends ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    void 제품을_저장한다() {
        final ProductRequest request = new ProductRequest("abc", BigDecimal.valueOf(1000));

        final ProductResponse productResponse = productService.create(request);

        assertThat(productResponse.getId()).isNotNull();
    }

    @Test
    void 제품_가격이_음수면_예외를_발생한다() {
        final ProductRequest request = new ProductRequest("abc", BigDecimal.valueOf(-200));

        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_제품을_조회한다() {
        final List<ProductResponse> expected = productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        final List<ProductResponse> actual = productService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
