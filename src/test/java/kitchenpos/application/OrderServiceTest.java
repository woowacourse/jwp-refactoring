package kitchenpos.application;

import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEMS;
import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEM_REQUESTS;
import static kitchenpos.application.ServiceTestFixture.ORDER_LINE_ITEM_REQUEST_NOT_EXIST_MENU_ID;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {

    @Nested
    class CreateOrderServiceTest extends ServiceTest {

        @Test
        void create_fail_when_orderLineItems_has_zero_size() {
            final OrderRequest orderRequest = new OrderRequest(1L, "COOKING", new ArrayList<>());

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderLineItems_is_null() {
            final OrderRequest orderRequest = new OrderRequest(1L, "COOKING", null);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_menuIds_size_is_different_from_orderLineItems() {
            List<OrderLineItemRequest> differentSizeOrderLineItemRequests = new ArrayList<>(ORDER_LINE_ITEM_REQUESTS);
            differentSizeOrderLineItemRequests.add(ORDER_LINE_ITEM_REQUEST_NOT_EXIST_MENU_ID);

            final OrderRequest orderRequest = new OrderRequest(1L, "COOKING", differentSizeOrderLineItemRequests);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTableId_not_exist() {
            final OrderRequest orderRequest = new OrderRequest(100L, "COOKING",
                    ORDER_LINE_ITEM_REQUESTS);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_fail_when_orderTable_is_empty() {
            final OrderRequest orderRequest = new OrderRequest(100L, "COOKING",
                    ORDER_LINE_ITEM_REQUESTS);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void create_success() {
            OrderTable orderTable = new OrderTable(1L, 5, false);
            OrderTable savedOrderTable = orderTableDao.save(orderTable);

            OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), "COOKING", ORDER_LINE_ITEM_REQUESTS);
            Order savedOrder = orderService.create(orderRequest);

            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING);
        }
    }

    @Nested
    class ListOrderServiceTest extends ServiceTest {
        @Test
        void list_success() {
            assertThat(menuService.list()).hasSize(6);
        }
    }

    @Nested
    class ChangeOrderStatusTest extends ServiceTest {

        Order order;

        @BeforeEach
        void setup() {
            order = new Order(1L, COOKING,
                    LocalDateTime.now(), ORDER_LINE_ITEMS);
        }

        @Test
        void changeOrderStatus_fail_when_orderId_not_exist() {
            Long orderId = 1000L;

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void changeOrderStatus_success() {
            OrderTable orderTable = new OrderTable(1L, 5, false);
            OrderTable savedOrderTable = orderTableDao.save(orderTable);

            OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), "COOKING", ORDER_LINE_ITEM_REQUESTS);

            Order savedOrder = orderService.create(orderRequest);

            Order newOrder = new Order(savedOrder.getId(), savedOrderTable.getId(), MEAL,
                    savedOrder.getOrderedTime(), savedOrder.getOrderLineItems());

            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

            assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL);
        }
    }
}
