package kitchenpos.domain.menu;

import static kitchenpos.exception.MenuException.NegativeQuantityException;
import static kitchenpos.exception.MenuException.NoMenuProductException;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuProductTest {

    @Test
    void 메뉴_상품의_상품은_없으면_예외가_발생_한다() {
        // given
        final Product product = null;
        final Long quantity = 1L;

        // expected
        assertThatThrownBy(() -> new MenuProduct(product, quantity))
                .isInstanceOf(NoMenuProductException.class);
    }

    @Test
    void 메뉴_상품의_수량은_음수일_시_예외가_발생_한다() {
        // given
        final Product product = 상품("상품", BigDecimal.valueOf(1000));
        final Long quantity = -1L;

        // expected
        assertThatThrownBy(() -> new MenuProduct(product, quantity))
                .isInstanceOf(NegativeQuantityException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1})
    void 메뉴_상품은_상품과_0이상의_가격을_받아_생성된다(final long quantity) {
        // given
        final Product product = 상품("상품", BigDecimal.valueOf(1000));

        // expected
        assertDoesNotThrow(() -> new MenuProduct(product, quantity));
    }

    @Test
    void 메뉴_상품은_수량과_상품_가격을_곱한_총합을_계산한다() {
        // given
        final Product product = 상품("상품", BigDecimal.valueOf(1000));
        final Long quantity = 10L;

        // when
        final MenuProduct menuProduct = new MenuProduct(product, quantity);
        final Price result = menuProduct.getPrice();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new Price(BigDecimal.valueOf(10000)));
    }
}
