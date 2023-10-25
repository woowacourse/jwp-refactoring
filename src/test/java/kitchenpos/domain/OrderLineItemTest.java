package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderLineItemTest {

    @Test
    void 주문_상품_생성() {
        final MenuProduct menuProduct = new MenuProduct(null, Product.of("치킨", BigDecimal.valueOf(10_000L)), 1L);
        final Menu menu = Menu.of("치킨", BigDecimal.valueOf(10_000L), null, List.of(menuProduct));
        final OrderTable orderTable = new OrderTable(null, 10, false);
        final OrderLineItem orderLineItem1 = new OrderLineItem(null, menu, 10L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(null, menu, 10L);
        final Order order = new Order(orderTable, LocalDateTime.now(), List.of(orderLineItem1, orderLineItem2));

        assertDoesNotThrow(
                () -> new OrderLineItem(order, menu, 10)
        );
    }
}
