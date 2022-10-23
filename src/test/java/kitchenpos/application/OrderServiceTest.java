package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEMS;
import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEM_NOT_EXIST_MENU_ID;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderServiceTest extends ServiceTest {

    @Nested
    class CreateOrderServiceTest {

        Order order;

        @BeforeEach
        void setup() {
            order = new Order();
            order.setOrderStatus(COOKING.name());
            order.setOrderLineItems(ORDER_LINE_ITEMS);
            order.setOrderTableId(1L);
        }

        @Test
        void create_fail_when_orderLineItems_has_zero_size() {
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderLineItems_is_null() {
            order.setOrderLineItems(null);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_menuIds_size_is_different_from_orderLineItems() {
            List<OrderLineItem> differentSizeOrderLineItems = new ArrayList<>(ORDER_LINE_ITEMS);
            differentSizeOrderLineItems.add(ORDER_LINE_ITEM_NOT_EXIST_MENU_ID);
            order.setOrderLineItems(differentSizeOrderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTableId_not_exist() {
            order.setOrderTableId(100L);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_is_empty() {
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);
            orderTable.setEmpty(false);
            OrderTable savedOrderTable =  orderTableDao.save(orderTable);
            order.setOrderTableId(savedOrderTable.getId());
            Order savedOrder = orderService.create(order);

            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
        }
    }

    @Nested
    class ListOrderServiceTest {
        @Test
        void list_success() {
            assertThat(menuService.list()).hasSize(6);
        }
    }

    @Nested
    class ChangeOrderStatusTest {

        Order order;

        @BeforeEach
        void setup() {
            order = new Order();
            order.setOrderStatus(COOKING.name());
            order.setOrderLineItems(ORDER_LINE_ITEMS);
            order.setOrderTableId(1L);
        }

        @Test
        void changeOrderStatus_fail_when_orderId_not_exist() {
            Long orderId = 1000L;

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeOrderStatus_success() {
            OrderTable orderTable = new OrderTable();
            orderTable.setNumberOfGuests(5);
            orderTable.setEmpty(false);
            OrderTable savedOrderTable =  orderTableDao.save(orderTable);
            order.setOrderTableId(savedOrderTable.getId());

            Order savedOrder = orderService.create(order);

            order.setOrderStatus(MEAL.name());

            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), order);

            assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
        }
    }
}
