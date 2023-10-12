package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Test
    void create_order_success() {
        // given
        final Menu savedMenu = generateMenu("chicken");
        final OrderTable savedOrderTable = generateOrderTable(3);
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(List.of(generateOrderLineItem(savedMenu)));
        order.setOrderTableId(savedOrderTable.getTableGroupId());

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
        assertThat(savedOrder.getOrderLineItems()).hasSize(1);
    }

    @Nested
    class create_order_failure {

        @Test
        void order_line_item_empty() {
            // given
            final Order order = new Order();
            order.setOrderTableId(1L);
            order.setOrderLineItems(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_line_item_size_mismatch_with_actual_menus() {
            // given
            final OrderLineItem orderLineItemWithoutMenu = new OrderLineItem();
            orderLineItemWithoutMenu.setMenuId(1000L);
            final Order order = new Order();
            order.setOrderTableId(1L);
            order.setOrderLineItems(List.of(orderLineItemWithoutMenu));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table_is_not_exist() {
            // given
            final Menu savedMenu = generateMenu("chicken");
            final Order order = new Order();
            order.setOrderTableId(1L);
            order.setOrderLineItems(List.of(generateOrderLineItem(savedMenu)));
            order.setOrderTableId(1000L);

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table_state_is_empty() {
            // given
            final Menu savedMenu = generateMenu("chicken");
            final OrderTable savedOrderTable = generateOrderTable(3, true);
            final Order order = new Order();
            order.setOrderTableId(1L);
            order.setOrderLineItems(List.of(generateOrderLineItem(savedMenu)));
            order.setOrderTableId(savedOrderTable.getTableGroupId());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
