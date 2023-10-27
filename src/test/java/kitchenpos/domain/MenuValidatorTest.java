package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuValidatorTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 메뉴는_0_이상의_가격을_가진다() {
        final Menu menu = new Menu("스키야키", BigDecimal.valueOf(-1), 1L, new ArrayList<>());

        assertThatThrownBy(() -> new MenuValidator(productRepository).validate(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
