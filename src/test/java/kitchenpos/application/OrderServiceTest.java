package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderLineItemRequest;
import kitchenpos.dto.order.CreateOrderRequest;

class OrderServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class CreateMethod {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 주문을 생성한다.")
        void create() {
            // given
            Menu savedMenu = createAndSaveMenu();
            OrderTable savedOrderTable = createAndSaveOrderTable();

            CreateOrderRequest request = createOrderCreateRequest(
                savedOrderTable.getId(),
                savedMenu.getId(),
                10
            );

            // when
            Order savedOrder = orderService.create(request);

            // then
            assertThat(savedOrder.getId()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않는 메뉴 id인 경우 예외가 발생한다.")
        void invalidOrderLineItemId() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable();

            CreateOrderRequest request = createOrderCreateRequest(
                savedOrderTable.getId(),
                0L,
                10
            );

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴입니다.");
        }

        @Test
        @DisplayName("존재하지 않는 주문 테이블 id인 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            // given
            Menu savedMenu = createAndSaveMenu();

            CreateOrderRequest request = createOrderCreateRequest(
                0L,
                savedMenu.getId(),
                10
            );

            // when, then
            assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블 입니다.");
        }

    }

    @Nested
    @DisplayName("list()")
    class ListMethod {

        @Test
        @DisplayName("전체 주문을 조회한다.")
        void list() {
            List<Order> orders = orderService.list();
            assertThat(orders).isNotNull();
        }

    }

    @Nested
    @DisplayName("changeOrderStatus()")
    class ChangeOrderStatusMethod {

        @Test
        @DisplayName("특정 주문의 주문 상태를 변경한다.")
        void changeOrderStatus() {
            // given
            Order savedOrder = createAndSaveOrder();
            ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");

            // when
            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), request);

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
        }

        @Test
        @DisplayName("존재하지 않는 order id인 경우 예외가 발생한다.")
        void wrongInvalidOrderId() {
            // given
            ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 주문입니다.");
        }

    }

    private Order createAndSaveOrder() {
        Menu savedMenu = createAndSaveMenu();
        OrderTable savedOrderTable = createAndSaveOrderTable();
        Order savedOrder = orderDao.save(new Order(savedOrderTable.getId()));

        OrderLineItem orderLineItem = new OrderLineItem(savedOrder.getId(), savedMenu.getId(), 10);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        savedOrder.setOrderLineItems(new ArrayList<OrderLineItem>() {{
            add(savedOrderLineItem);
        }});

        return savedOrder;
    }

    private Menu createAndSaveMenu() {
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));
        Menu menu = new Menu("menu", new BigDecimal(0), savedMenuGroup, new HashMap<>());

        return menuDao.save(menu);
    }

    private OrderTable createAndSaveOrderTable() {
        OrderTable orderTable = new OrderTable(10, false);
        return orderTableDao.save(orderTable);
    }

    private CreateOrderRequest createOrderCreateRequest(long orderTableId, long menuId, long quantity) {
        CreateOrderLineItemRequest orderLineItem = new CreateOrderLineItemRequest(menuId, quantity);
        return new CreateOrderRequest(
            orderTableId,
            new ArrayList<CreateOrderLineItemRequest>() {{
                add(orderLineItem);
            }}
        );
    }

}
