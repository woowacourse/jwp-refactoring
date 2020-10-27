package kitchenpos.domain;

import kitchenpos.domain.exceptions.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("Menu 생성 실패 - 가격이 null일 경우")
    @Test
    public void createFailPriceNull() {
        assertThatThrownBy(() -> new Menu("파스타", null, new MenuGroup("양식")))
                .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("Menu 생성 실패 - 가격이 0보다 작을 경우")
    @Test
    public void createFailPriceMinus() {
        assertThatThrownBy(() -> new Menu("파스타", BigDecimal.valueOf(-1), new MenuGroup("양식")))
                .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("Menu 생성")
    @Test
    public void createMenu() {
        Menu menu = new Menu("파스타", BigDecimal.valueOf(10_000L), new MenuGroup("양식"));

        assertThat(menu).isNotNull();
        assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10_000L));
        assertThat(menu.getMenuGroup().getName()).isEqualTo("양식");
    }
}