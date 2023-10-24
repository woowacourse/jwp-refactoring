package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.vo.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("MenuProducts를 추가한다.")
    void addMenuProducts() {
        // given
        final Menu menu = new Menu("name", BigDecimal.ZERO, new MenuGroup("menuGroup"));

        // when, then
        assertThatNoException()
                .isThrownBy(() -> menu.addMenuProducts(Collections.emptyList()));
    }

    @Test
    @DisplayName("가격이 잘못되면 예외가 발생한다.")
    void validateMenuPrice() {
        // given
        final Menu menu = new Menu("name", BigDecimal.TEN, new MenuGroup("menuGroup"));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menu.addMenuProducts(Collections.emptyList()));
    }
}
