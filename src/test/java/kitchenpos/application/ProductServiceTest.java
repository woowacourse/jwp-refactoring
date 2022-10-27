package kitchenpos.application;

import static kitchenpos.application.TestFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given, when
        final Product actual = productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));

        // then
        assertThat(actual.getName()).isEqualTo("테스트-상품");
    }

    @Test
    void 상품_생성시_상품_금액이_음수_인_경우_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> productService.create(상품_생성("테스트-상품", BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성시_상품_금액이_null_인_경우_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> productService.create(상품_생성("테스트-상품", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_전체를_조회한다() {
        // given
        final Product expected = 상품을_저장한다(상품_생성("테스트-상품", BigDecimal.valueOf(99999)));

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).usingElementComparatorOnFields("name")
                .containsExactly(expected);
    }
}
