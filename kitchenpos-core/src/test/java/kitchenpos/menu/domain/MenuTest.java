package kitchenpos.menu.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MenuTest {

    @Test
    void Menu를_생성할_수_있다() {
        //when, then
        assertDoesNotThrow(() -> new Menu("디노 세트", new MenuPrice(new BigDecimal(8000)), null));
    }

}
