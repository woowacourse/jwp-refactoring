package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.OrderLineItemsRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static kitchenpos.MenuFixture.*;
import static kitchenpos.OrderFixture.createOrder;
import static kitchenpos.OrderFixture.createOrderLineItem;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.ProductFixture.createProduct2;
import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends SpringBootTestSupport {

    @Autowired
    private OrderService orderService;

    @DisplayName("주문 생성은")
    @Nested
    class Create extends SpringBootTestSupport {

        private OrderRequest request;
        private MenuGroup menuGroup1;
        private Product product1;
        private Menu menu1;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            menuGroup1 = save(createMenuGroup1());
            product1 = save(createProduct1());
            menu1 = save(createMenu1(menuGroup1, Collections.singletonList(product1)));
            orderTable = save(createOrderTable());
        }

        @DisplayName("존재하지 않는 메뉴를 포함한 주문 항목이 포함된 경우 생성할 수 없다.")
        @Test
        void createExceptionIfHasNotExistMenu() {
            request = new OrderRequest(orderTable.getId(), Collections.singletonList(new OrderLineItemsRequest(0L, 2)));

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블에 속한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfHasNotExistOrder() {
            request = new OrderRequest(0L, Collections.singletonList(new OrderLineItemsRequest(menu1.getId(), 2)));

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블의 주문은 생성할 수 없다.")
        @Test
        void createExceptionIfEmptyOrderTable() {
            orderTable = save(new OrderTable(3, true));
            request = new OrderRequest(orderTable.getId(), Collections.singletonList(new OrderLineItemsRequest(menu1.getId(), 2)));

            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
        @Test
        void create() {
            request = new OrderRequest(orderTable.getId(), Collections.singletonList(new OrderLineItemsRequest(menu1.getId(), 2)));

            final OrderResponse actual = orderService.create(request);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderTable().getId()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        MenuGroup menuGroup1 = save(createMenuGroup1());
        MenuGroup menuGroup2 = save(createMenuGroup2());
        Product product1 = save(createProduct1());
        Product product2 = save(createProduct2());
        Menu menu1 = save(createMenu1(menuGroup1, Collections.singletonList(product1)));
        Menu menu2 = save(createMenu2(menuGroup2, Collections.singletonList(product2)));
        OrderTable orderTable = save(createOrderTable());
        Order order1 = save(createOrder(orderTable, Collections.singletonList(createOrderLineItem(menu1))));
        Order order2 = save(createOrder(orderTable, Collections.singletonList(createOrderLineItem(menu2))));

        List<OrderResponse> actual = orderService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).getId()).isEqualTo(order1.getId()),
                () -> assertThat(actual.get(0).getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.get(1).getId()).isEqualTo(order2.getId()),
                () -> assertThat(actual.get(1).getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문 상태 변경은")
    @Nested
    class ChangeStatus extends SpringBootTestSupport {

        private OrderStatusRequest request;
        private MenuGroup menuGroup;
        private Product product;
        private Menu menu;
        private OrderTable orderTable;
        private Order order;

        @BeforeEach
        void setUp() {
            request = new OrderStatusRequest(OrderStatus.MEAL.name());
            menuGroup = save(createMenuGroup1());
            product = save(createProduct1());
            menu = save(createMenu1(menuGroup, Collections.singletonList(product)));
            orderTable = save(createOrderTable());
            order = save(createOrder(orderTable, Collections.singletonList(createOrderLineItem(menu))));
        }

        @DisplayName("존재하지 않는 주문일 경우 변경할 수 없다.")
        @Test
        void changeOrderStatusExceptionIfNotExist() {
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산 완료된 주문일 경우 변경할 수 없다.")
        @Test
        void changeOrderStatusExceptionIfCompletion() {
            order = save(createOrder(orderTable, OrderStatus.COMPLETION, Collections.singletonList(createOrderLineItem(menu))));

            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족할 경우 변경할 수 있다.")
        @Test
        void changeOrderStatus() {
            final OrderResponse actual = orderService.changeOrderStatus(order.getId(), request);

            assertThat(actual.getOrderStatus()).isEqualTo(request.getOrderStatus());
        }
    }
}
