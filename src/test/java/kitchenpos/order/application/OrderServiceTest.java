package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.fixture.OrderFixture.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class OrderServiceTest {

    @MockBean
    private MenuRepository mockMenuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderService orderService;

    private Long menuId = 1L;
    private Long orderTableId;

    @BeforeEach
    void setUp() {
        Mockito.when(mockMenuRepository.existsById(menuId)).thenReturn(true);
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        orderTableId = orderTable.getId();
    }

    @DisplayName("주문 생성")
    @Nested
    class createOrder {

        @DisplayName("주문을 생성한다.")
        @Test
        void create() {
            OrderRequest request = createOrderRequest(orderTableId, menuId);
            OrderResponse result = orderService.create(request);
            SoftAssertions.assertSoftly(it -> {
                it.assertThat(result).isNotNull();
                it.assertThat(result.getId()).isNotNull();
                it.assertThat(result.getOrderStatus()).isEqualTo(request.getOrderStatus());
            });
        }

        @DisplayName("주문 항목이 1개 이상이어야한다.")
        @Test
        void createWithInvalidOrderItemList() {
            OrderRequest request = createOrderRequest(orderTableId, Collections.emptyList());
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목의 메뉴가 존재해야 한다.")
        @Test
        void createWithNonexistentMenu() {
            menuId = Long.MAX_VALUE;
            OrderRequest request = createOrderRequest(orderTableId, menuId);
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블은 비어있지 않아야한다.")
        @Test
        void createWithEmptyTable() {
            OrderTable emptyTable = orderTableRepository.save(new OrderTable(1, true));
            OrderRequest request = createOrderRequest(orderTableId, emptyTable.getId());
            assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 반환한다.")
    @Test
    void list() {
        OrderResponse order1 = orderService.create(createOrderRequest(orderTableId, menuId));
        OrderResponse order2 = orderService.create(createOrderRequest(orderTableId, menuId));

        List<OrderResponse> expected = Arrays.asList(order1, order2);
        List<OrderResponse> result = orderService.list();
        SoftAssertions.assertSoftly(it -> {
            it.assertThat(result).hasSize(expected.size());
            it.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        });
    }

    @DisplayName("주문 상태 변경")
    @Nested
    class ChangeOrderStatus {

        private Long orderId;

        @BeforeEach
        void setUp() {
            orderId = orderService.create(createOrderRequest(orderTableId, menuId)).getId();
        }

        @DisplayName("주문의 상태를 변경한다.")
        @Test
        void changeOrderStatus() {
            OrderStatus newStatus = OrderStatus.MEAL;
            OrderRequest request = createOrderRequest(orderTableId, newStatus, menuId);
            OrderResponse result = orderService.changeOrderStatus(orderId, request);
            SoftAssertions.assertSoftly(it -> {
                it.assertThat(result).isNotNull();
                it.assertThat(result.getOrderStatus()).isEqualTo(newStatus);
            });
        }

        @DisplayName("주문 완료 상태의 주문은 상태를 변경할 수 없다.")
        @Test
        void changeOrderStatusInCompletion() {
            orderService.changeOrderStatus(orderId, createOrderRequest(orderTableId, OrderStatus.COMPLETION, menuId));

            OrderRequest updateRequest = createOrderRequest(orderTableId, OrderStatus.MEAL, menuId);
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, updateRequest)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderLineItemRepository.deleteAll();
        orderTableRepository.deleteAll();
    }
}
