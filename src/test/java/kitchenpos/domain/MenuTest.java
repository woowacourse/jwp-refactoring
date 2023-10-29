package kitchenpos.domain;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void Menu를_생성한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("한식");

        // when
        final Menu menu = new Menu("상품", new Price(BigDecimal.valueOf(1000)), menuGroup, null);

        // then
        assertThat(menu.getPrice().getValue()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void Menu_생성_시_입력된_가격이_null이면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("한식");
        final BigDecimal value = null;

        // when, then
        assertThatThrownBy(() -> new Menu("상품", new Price(value), menuGroup, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Product_생성_시_입력된_가격이_0보다_작으면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("한식");
        final BigDecimal value = BigDecimal.valueOf(-1000);

        // when, then
        assertThatThrownBy(() -> new Menu("상품", new Price(value), menuGroup, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}