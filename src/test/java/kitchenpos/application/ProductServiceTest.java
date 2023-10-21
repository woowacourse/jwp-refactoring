package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest implements ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품_생성_시_상품의_가격이_없으면_예외가_발생한다() {
        // given
        final Product product = 상품("후라이드치킨", null);

        // expected
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격은_0원_보다_작다면_예외가_발생한다() {
        // given
        final Product product = 상품("후라이드치킨", BigDecimal.valueOf(-1));

        // expected
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이름과_가격을_가진_상품은_생성_후_ID를_가진다() {
        // given
        final Product product = 상품("후라이드치킨", BigDecimal.valueOf(1000));

        // when
        final Product savedProduct = productService.create(product);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(product.getId()).isNull();
            softly.assertThat(savedProduct.getId()).isNotNull();
        });
    }
}
