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

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusUpdateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.OrderRepository;
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
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    MenuGroup 두마리메뉴;
    Menu 후라이드_양념치킨_두마리세트;
    Product 후라이드;
    Product 양념치킨;
    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        두마리메뉴 = menuGroupDao.save(MenuGroupFixture.두마리메뉴.toMenuGroup());

        후라이드 = productDao.save(ProductFixture.후라이드.toProduct());
        양념치킨 = productDao.save(ProductFixture.양념치킨.toProduct());

        Menu menu = MenuFixture.후라이드_양념치킨_두마리세트.toMenu(두마리메뉴.getId(), 후라이드.getId(), 양념치킨.getId());
        후라이드_양념치킨_두마리세트 = menuDao.save(menu);

        OrderTable newOrderTable = new OrderTable(3, false);
        orderTable = orderTableDao.save(newOrderTable);
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
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderLineItems에 존재하지 않는 메뉴가 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistMenu() {
            Long notExistId = 0L;
            List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(notExistId, 3L));
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
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
            OrderTable emptyTable = orderTableDao.save(newEmptyTable);

            List<OrderLineItemRequest> orderLineItemRequests = List.of(
                    new OrderLineItemRequest(후라이드_양념치킨_두마리세트.getId(), 3L));
            OrderRequest order = new OrderRequest(emptyTable.getId(), orderLineItemRequests);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        List<OrderResponse> actual = orderService.list();

        assertThat(actual).hasSize(0);
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
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Order createOrder() {
            Long orderTableId = orderTable.getId();
            changeTableToNotEmpty(orderTableId);

            List<OrderLineItem> orderLineItems = List.of(
                    new OrderLineItem(후라이드_양념치킨_두마리세트.getId(), 3L));
            Order orderRequest = Order.create(orderTableId, orderLineItems);

            return orderRepository.save(orderRequest);
        }
    }

    void changeTableToNotEmpty(Long tableId) {
        OrderTable orderTable = orderTableDao.findById(tableId)
                .orElseThrow();
        orderTable.changeEmptyStatus(false);
        orderTable.changeNumberOfGuests(5);

        orderTableDao.save(orderTable);
    }
}
