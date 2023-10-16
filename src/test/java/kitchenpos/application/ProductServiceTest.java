package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.persistence.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_저장_성공() {
        // given
        ProductCreateRequest expected = new ProductCreateRequest("고추바사삭", BigDecimal.valueOf(10000L));

        // when
        Product actual = productService.create(expected);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @ParameterizedTest
    @ValueSource(longs = {-10000L, -1L})
    void 상품_저장_실패_가격이_음수(Long price) {
        // given
        ProductCreateRequest given = new ProductCreateRequest("고추바사삭", BigDecimal.valueOf(price));

        // when && then
        assertThatThrownBy(() -> productService.create(given))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품_조회() {
        // given
        List<Product> expected = new ArrayList<>();
        expected.add(productRepository.save(new Product("고추바사삭", 10000L)));
        expected.add(productRepository.save(new Product("뿌링클", 20000L)));
        expected.add(productRepository.save(new Product("맛초킹", 3000L)));

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
