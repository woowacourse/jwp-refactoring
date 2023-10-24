package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderLineItemTest {

    @Test
    void 주문_상품_생성() {
        final Menu menu = Menu.of("치킨", 10_000L, null);
        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

        assertDoesNotThrow(
                () -> new OrderLineItem(order, menu, 10)
        );
    }
}
