package kitchenpos.domain.menu;

import kitchenpos.DomainTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
class MenuPriceTest {
    @Test
    void 메뉴_가격이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuPrice(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0원_보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuPrice(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_1000조_이상이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuPrice(BigDecimal.valueOf(Math.pow(10, 17))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품들의_금액을_합한_가격보다_크면_예외가_발생한다() {
        final MenuPrice menuPrice = new MenuPrice(BigDecimal.TEN);

        assertThatThrownBy(() -> menuPrice.validateMoreThanMenuProductsPrice(BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
