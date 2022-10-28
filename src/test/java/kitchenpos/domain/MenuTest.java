package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuTest {

    private final MenuGroup menuGroup = new MenuGroup("menuGroup");

    private final Map<Product, Long> menuProducts = new HashMap<Product, Long>() {{
        put(new Product("product1", new BigDecimal(1000)), 1L);
        put(new Product("product1", new BigDecimal(1000)), 1L);
    }};

    @Test
    @DisplayName("예외사항이 존재하지 않는 경우 객체를 생성한다.")
    void menu() {
        assertDoesNotThrow(() -> new Menu("name", new BigDecimal(1000), menuGroup, menuProducts));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("name이 비어있을 경우 예외가 발생한다.")
    void emptyName(String name) {
        assertThatThrownBy(() -> new Menu(name, new BigDecimal(1000), menuGroup, menuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 이름은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 null일 경우 예외가 발생한다.")
    void nullPrice() {
        assertThatThrownBy(() -> new Menu("name", null, menuGroup, menuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 가격은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("price가 0원 미만일 경우 예외가 발생한다.")
    void negativePrice() {
        assertThatThrownBy(() -> new Menu("name", new BigDecimal(-1), menuGroup, menuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 0원 미만일 수 없습니다.");
    }

    @Test
    @DisplayName("각 상품의 합보다 가격이 큰 값일 경우 예외가 발생한다.")
    void biggerThanProductPriceSum() {
        assertThatThrownBy(() -> new Menu("name", new BigDecimal(5000), menuGroup, menuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("각 상품 가격의 합보다 큰 가격을 적용할 수 없습니다.");
    }

}
