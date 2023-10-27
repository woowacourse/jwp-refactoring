package kitchenpos.prodcut.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.product.Product;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.persistence.ProductRepository;
import kitchenpos.product.request.ProductCreateRequest;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품_저장_성공() {
        // given
        var request = new ProductCreateRequest("고추바사삭", BigDecimal.valueOf(10000L));

        // when
        Product actual = productService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @ParameterizedTest
    @ValueSource(longs = {-10000L, -1L})
    void 가격이_음수면_상품_저장시_예외(Long price) {
        // given
        var request = new ProductCreateRequest("고추바사삭", BigDecimal.valueOf(price));

        // when && then
        assertThatThrownBy(() -> productService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품_조회() {
        // given
        List<Product> expected = new ArrayList<>();
        expected.add(productRepository.save(new Product("고추바사삭", BigDecimal.valueOf(10000L))));
        expected.add(productRepository.save(new Product("뿌링클", BigDecimal.valueOf(20000L))));
        expected.add(productRepository.save(new Product("맛초킹", BigDecimal.valueOf(3000L))));

        // when
        List<Product> actual = productService.list();

        // then
        Assertions.assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
