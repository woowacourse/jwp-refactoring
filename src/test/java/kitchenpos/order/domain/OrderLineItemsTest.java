package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.exception.OrderLineItemDuplicateException;
import kitchenpos.order.exception.OrderLineItemsEmptyException;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderLineItems 단위 테스트")
class OrderLineItemsTest {

    @DisplayName("OrderLineItems를 생성할 때")
    @Nested
    class Create {

        @DisplayName("OrderLineItem이 1개도 없을 경우 예외가 발생한다.")
        @Test
        void emptyException() {
            // when, then
            assertThatThrownBy(() -> new OrderLineItems(new ArrayList<>()))
                .isExactlyInstanceOf(OrderLineItemsEmptyException.class);
        }

        @DisplayName("OrderLineItem과 Menu의 개수가 다를 경우 예외가 발생한다.")
        @Test
        void orderLineItemCountMenuCountNonMatchException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            Order order = new Order(orderTable);
            MenuGroup menuGroup = new MenuGroup("쩌는 그룹");
            Menu menu = new Menu("대박 메뉴", BigDecimal.ONE, menuGroup);
            OrderLineItemQuantity quantity = new OrderLineItemQuantity(1L);

            OrderLineItem orderLineItem1 = new OrderLineItem(1L, order, menu, quantity);
            OrderLineItem orderLineItem2 = new OrderLineItem(2L, order, menu, quantity);

            // when, then
            assertThatThrownBy(() -> new OrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2)))
                .isExactlyInstanceOf(OrderLineItemDuplicateException.class);
        }
    }
}
