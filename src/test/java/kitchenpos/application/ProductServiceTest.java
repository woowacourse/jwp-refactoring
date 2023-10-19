package kitchenpos.application;

import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTestHelper {

    @Test
    void 상품을_등록한다() {
        // given
        final Product product1 = 상품_등록("상품1", 1000L);
        final Product product2 = 상품_등록("상품2", 1000L);

        // when
        final List<Product> products = 상품_목록_조회();

        // then
        assertThat(products).usingElementComparatorIgnoringFields()
                .contains(product1, product2);
    }

    @Test
    void 상품_가격이_null이면_등록할_수_없다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 상품_등록("상품", null));
    }

    @Test
    void 상품_가격이_0보다_작으면_등록할_수_없다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 상품_등록("상품", -1L));
    }
}
