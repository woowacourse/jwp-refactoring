package kitchenpos.application;

import static kitchenpos.fixture.Fixture.OrderTable.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.fixture.Fixture.Menu;
import kitchenpos.fixture.Fixture.OrderTable;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    private List<OrderLineItemRequest> orderLineItemRequests;

    @BeforeEach
    void init() {
        tableService.changeEmpty(첫번째_테이블, false);
        orderLineItemRequests = List.of(
            new OrderLineItemRequest(Menu.후라이드_치킨, 1),
            new OrderLineItemRequest(Menu.양념_치킨, 1)
        );
    }

    @DisplayName("주문을")
    @Nested
    class CreateTest {

        @DisplayName("생성한다.")
        @Test
        void create() {
            // given
            OrderRequest orderRequest = new OrderRequest(첫번째_테이블, orderLineItemRequests);

            // when
            OrderResponse actual = orderService.create(orderRequest);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(actual.getOrderLineItems()).hasSize(2),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(첫번째_테이블)
            );
        }

        @DisplayName("메뉴 상품 1개 미만으로 생성할 수 없다.")
        @Test
        void emptyOrderLineItems() {
            // given
            OrderRequest orderRequest = new OrderRequest(첫번째_테이블, new ArrayList<>());

            // when then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품이 1개 이상 있어야 합니다.");
        }

        @DisplayName("없는 메뉴로 생성할 수 없다.")
        @Test
        void emptyMenu() {
            // given
            List<OrderLineItemRequest> notExistMenuRequest = new ArrayList<>(orderLineItemRequests);
            notExistMenuRequest.add(new OrderLineItemRequest(Menu.없는_메뉴, 1));

            OrderRequest orderRequest = new OrderRequest(첫번째_테이블, notExistMenuRequest);

            // when then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴가 포함되어 있습니다.");
        }

        @DisplayName("없는 테이블로 생성할 수 없다.")
        @Test
        void notExistOrderTable() {
            // given
            OrderRequest orderRequest = new OrderRequest(없는_테이블, orderLineItemRequests);

            // when then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
        }

        @DisplayName("빈 테이블로 생성할 수 없다.")
        @Test
        void emptyTable() {
            // given
            OrderRequest orderRequest = new OrderRequest(두번째_테이블, orderLineItemRequests);

            // when then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에선 주문을 생성할 수 없습니다.");
        }
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        OrderRequest orderRequest1 = new OrderRequest(첫번째_테이블, orderLineItemRequests);
        orderService.create(orderRequest1);

        OrderRequest orderRequest2 = new OrderRequest(첫번째_테이블, orderLineItemRequests);
        orderService.create(orderRequest2);

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("주문 상태는")
    @Nested
    class ChangeStatusTest {

        private Long orderId;

        @BeforeEach
        void createOrder() {
            OrderRequest orderRequest = new OrderRequest(첫번째_테이블, orderLineItemRequests);
            OrderResponse orderResponse = orderService.create(orderRequest);
            orderId = orderResponse.getId();
        }

        @DisplayName("식사 중으로 변경할 수 있다.")
        @Test
        void cookingToMeal() {
            // when
            OrderResponse actual = orderService.changeOrderStatus(orderId, OrderStatus.MEAL);

            // then
            Optional<Order> updatedOrder = orderDao.findById(orderId);
            assertAll(
                () -> assertThat(updatedOrder)
                    .map(Order::getOrderStatus)
                    .get()
                    .isEqualTo(OrderStatus.MEAL.name()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
            );
        }

        @DisplayName("완료로 변경할 수 있다.")
        @Test
        void mealToCompletion() {
            // when
            OrderResponse actual = orderService.changeOrderStatus(orderId, OrderStatus.COMPLETION);

            // then
            Optional<Order> updatedOrder = orderDao.findById(orderId);
            assertAll(
                () -> assertThat(updatedOrder)
                    .map(Order::getOrderStatus)
                    .get()
                    .isEqualTo(OrderStatus.COMPLETION.name()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name())
            );
        }

        @DisplayName("없는 주문에 대해 변경될 수 없다.")
        @Test
        void notExistOrder() {
            // when then
            assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문입니다.");
        }

        @DisplayName("완료 상태일 때 변경할 수 없다.")
        @Test
        void changeCompletionStatus() {
            // given
            orderService.changeOrderStatus(orderId, OrderStatus.COMPLETION);

            // when then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
