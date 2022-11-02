package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusUpdateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.SpringBootNestedTest;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private MenuGroup 두마리메뉴;
    private Menu 후라이드_양념치킨_두마리세트;
    private Product 후라이드;
    private Product 양념치킨;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        두마리메뉴 = menuGroupRepository.save(MenuGroupFixture.두마리메뉴.toMenuGroup());

        후라이드 = productRepository.save(ProductFixture.후라이드.toProduct());
        양념치킨 = productRepository.save(ProductFixture.양념치킨.toProduct());

        Menu menu = MenuFixture.후라이드_양념치킨_두마리세트.toMenu(두마리메뉴, 후라이드, 양념치킨);
        후라이드_양념치킨_두마리세트 = menuRepository.save(menu);

        OrderTable newOrderTable = new OrderTable(3, false);
        orderTable = orderTableRepository.save(newOrderTable);
    }

    @DisplayName("주문을 생성한다")
    @SpringBootNestedTest
    class CreateTest {

        @DisplayName("주문을 생성하면 ID가 할당된 Order객체가 반환된다")
        @Test
        void create() {
            List<OrderLineItemRequest> orderLineItemsRequests = List.of(
                    new OrderLineItemRequest(후라이드_양념치킨_두마리세트.getId(), 3L));
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemsRequests);

            OrderResponse actual = orderService.create(orderRequest);
            assertThat(actual).isNotNull();
        }

        @DisplayName("orderLineItems이 비어있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyOrderLineItems() {
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of());

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 항목이 비어있습니다.");
        }

        @DisplayName("orderLineItems에 존재하지 않는 메뉴가 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistMenu() {
            Long notExistId = 0L;
            List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(notExistId, 3L));
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 메뉴입니다.");
        }

        @DisplayName("존재하지 않는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistTable() {
            Long notExistId = 0L;
            List<OrderLineItemRequest> orderLineItemRequests = List.of(
                    new OrderLineItemRequest(후라이드_양념치킨_두마리세트.getId(), 3L));
            OrderRequest orderRequest = new OrderRequest(notExistId, orderLineItemRequests);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable(0, true);
            OrderTable emptyTable = orderTableRepository.save(newEmptyTable);

            List<OrderLineItemRequest> orderLineItemRequests = List.of(
                    new OrderLineItemRequest(후라이드_양념치킨_두마리세트.getId(), 3L));
            OrderRequest order = new OrderRequest(emptyTable.getId(), orderLineItemRequests);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블이 비어있습니다.");
        }
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        int numberOfOrders = 5;
        for (int i = 0; i < numberOfOrders; i++) {
             createOrder();
        }

        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(numberOfOrders);
    }

    @DisplayName("주문 상태를 변경한다")
    @SpringBootNestedTest
    class ChangeOrderStatusTest {

        @DisplayName("주문 상태를 변경한다")
        @Test
        void changeOrderStatus() {
            Order order = createOrder();
            OrderStatus expectedOrderStatus = OrderStatus.COMPLETION;

            OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest(
                    expectedOrderStatus.name());
            OrderResponse changedOrder = orderService.changeOrderStatus(order.getId(), orderStatusUpdateRequest);

            assertThat(changedOrder.getOrderStatus()).isEqualTo(expectedOrderStatus.name());
        }

        @DisplayName("존재하지 않는 주문일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistOrder() {
            Long notExistId = 0L;
            OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistId, orderStatusUpdateRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 주문입니다.");
        }
    }

    private Order createOrder() {
        Order order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));
        OrderLineItem orderLineItem = orderLineItemRepository.save(new OrderLineItem(후라이드_양념치킨_두마리세트, 3L));

        order.addOrderLineItem(orderLineItem);
        return order;
    }
}
