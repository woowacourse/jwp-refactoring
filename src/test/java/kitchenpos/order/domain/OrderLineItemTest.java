package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.exception.InvalidOrderLineItemException;
import kitchenpos.order.exception.InvalidQuantityException;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderLineItem 단위 테스트")
class OrderLineItemTest {

    @DisplayName("OrderLineItem을 생성할 때")
    @Nested
    class Create {

        @DisplayName("Order가 Null이면 예외가 발생한다.")
        @Test
        void oderNullException() {
            // given
            MenuGroup menuGroup = new MenuGroup("쩌는 그룹");
            Menu menu = new Menu("대박 메뉴", BigDecimal.ONE, menuGroup);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(null, menu, 5L))
                .isExactlyInstanceOf(InvalidOrderLineItemException.class);
        }

        @DisplayName("Menu가 Null이면 예외가 발생한다.")
        @Test
        void menuNullException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            Order order = new Order(orderTable);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(order, null, 5L))
                .isExactlyInstanceOf(InvalidOrderLineItemException.class);
        }

        @DisplayName("Quantity가 Null이면 예외가 발생한다.")
        @Test
        void quantityNullException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            Order order = new Order(orderTable);
            MenuGroup menuGroup = new MenuGroup("쩌는 그룹");
            Menu menu = new Menu("대박 메뉴", BigDecimal.ONE, menuGroup);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(order, menu, null))
                .isExactlyInstanceOf(InvalidQuantityException.class);
        }

        @DisplayName("Quantity가 음수면 예외가 발생한다.")
        @Test
        void quantityNegativeException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            Order order = new Order(orderTable);
            MenuGroup menuGroup = new MenuGroup("쩌는 그룹");
            Menu menu = new Menu("대박 메뉴", BigDecimal.ONE, menuGroup);

            // when, then
            assertThatThrownBy(() -> new OrderLineItem(order, menu, -1L))
                .isExactlyInstanceOf(InvalidQuantityException.class);
        }
    }
}
