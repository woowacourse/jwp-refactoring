package kitchenpos.order.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderLineItemTest {

    @Test
    void 주문_상품_생성() {
        final MenuProduct menuProduct = new MenuProduct(null, 1L);
        final Menu menu = Menu.of("치킨", BigDecimal.valueOf(10_000L), null, List.of(menuProduct));

        assertDoesNotThrow(
                () -> new OrderLineItem(menu.getId(), 10)
        );
    }
}
