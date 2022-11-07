package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuTest {

    @DisplayName("메뉴의 가격이 null 이거나 음수이면 예외가 발생한다.")
    @ParameterizedTest(name = "메뉴의 가격이 {0} 이면 예외가 발생한다.")
    @NullSource
    @ValueSource(strings = {"-1"})
    void construct_Exception_Price(BigDecimal price) {
        assertThatThrownBy(() -> new Menu("두마리메뉴", price, null, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 null 이거나 음수이면 안됩니다.");
    }

    @Test
    void changeMenuProducts() {
        final BigDecimal thresholdPrice = BigDecimal.valueOf(2000L);
        final Menu menu = new Menu("두마리메뉴", thresholdPrice.add(BigDecimal.ONE), new MenuGroup("메뉴그룹"), Collections.emptyList());

        assertThatThrownBy(() -> menu.changeMenuProducts(List.of(new MenuProduct(menu, new Product("페퍼로니", BigDecimal.valueOf(1000L)), 2L))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 안됩니다.");
    }
}
