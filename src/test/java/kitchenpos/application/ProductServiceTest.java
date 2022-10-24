package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given, when
        final Product product = productService.create(TestFixture.상품_생성("테스트-상품", BigDecimal.valueOf(99999)));

        // then
        assertThat(product.getName()).isEqualTo("테스트-상품");
    }

    @Test
    void 상품_생성시_상품_금액이_음수_인_경우_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> productService.create(TestFixture.상품_생성("테스트-상품", BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_생성시_상품_금액이_null_인_경우_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> productService.create(TestFixture.상품_생성("테스트-상품", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_전체를_조회한다() {
        // given
        final Product product = productService.create(TestFixture.상품_생성("테스트-상품", BigDecimal.valueOf(99999)));

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products).usingElementComparatorOnFields("name")
                .containsExactly(product);
    }
}
