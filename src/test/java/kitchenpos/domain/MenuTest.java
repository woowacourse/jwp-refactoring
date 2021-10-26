package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @DisplayName("상품의 가격이 Null이면 IllegalArgumentException을 발생시킨다.")
    @Test
    public void validateMenu_nullPrice() {
        Menu menu = new Menu("메뉴명", null, null);
        assertThatThrownBy(() -> {
            menu.validateMenuPrice();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수이면 IllegalArgumentException을 발생시킨다.")
    @Test
    public void validateMenu_negativePrice() {
        Menu menu = new Menu("메뉴명", new BigDecimal(-100), null);
        assertThatThrownBy(() -> {
            menu.validateMenuPrice();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
