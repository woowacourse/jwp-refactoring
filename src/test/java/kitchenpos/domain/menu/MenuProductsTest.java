package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.exception.InvalidMenuPriceException;
import kitchenpos.domain.exception.InvalidProductException;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductsTest {

    @Test
    void of_메서드는_유효한_데이터를_전달하면_menuProducts를_초기화한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 1L);
        final Price price = new Price(BigDecimal.TEN);

        // when
        final MenuProducts actual = MenuProducts.of(price, price, List.of(menuProduct));

        // then
        assertThat(actual.getValues()).hasSize(1);
    }

    @ParameterizedTest(name = "menuProducts가 {0}이면 예외가 발생한다.")
    @NullAndEmptySource
    void of_메서드는_menuProducts가_비어_있으면_예외가_발생한다(final List<MenuProduct> invalidMenuProducts) {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final Price price = new Price(BigDecimal.TEN);

        // when & then
        assertThatThrownBy(() -> MenuProducts.of(price, price, invalidMenuProducts))
                .isInstanceOf(InvalidProductException.class);
    }

    @Test
    void of_메서드는_menuProducts의_가격보다_menu의_가격이_클_경우_예외가_발생한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 1L);
        final Price totalProductPrice = new Price(BigDecimal.ZERO);
        final Price invalidMenuPrice = new Price(BigDecimal.TEN);

        // when & then
        assertThatThrownBy(() -> MenuProducts.of(totalProductPrice, invalidMenuPrice, List.of(menuProduct)))
                .isInstanceOf(InvalidMenuPriceException.class);
    }
}
