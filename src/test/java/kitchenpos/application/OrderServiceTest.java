package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.OrderLineItemsRequest;
import kitchenpos.ui.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static kitchenpos.MenuFixture.*;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.ProductFixture.createProduct2;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class OrderServiceTest extends SpringBootTestSupport {

    @Autowired
    private OrderService orderService;

    //    @Mock
//    private MenuDao menuDao;
//
//    @Mock
//    private OrderDao orderDao;
//
//    @Mock
//    private OrderLineItemDao orderLineItemDao;
//
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @DisplayName("주문 목록을 조회할 수 있다.")
//    @Test
//    void list() {
//        Order order1 = createOrder();
//        Order order2 = createOrder();
//        when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));
//
//        List<Order> actual = orderService.list();
//
//        assertAll(
//                () -> assertThat(actual).hasSize(2),
//                () -> assertThat(actual).containsExactly(order1, order2)
//        );
//    }
//
    @DisplayName("주문 생성은")
    @Nested
    class Create extends SpringBootTestSupport {

        private OrderRequest request;
        private MenuGroup menuGroup1;
        private MenuGroup menuGroup2;
        private Product product1;
        private Product product2;
        private Menu menu1;
        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            menuGroup1 = save(createMenuGroup1());
            menuGroup2 = save(createMenuGroup2());
            product1 = save(createProduct1());
            product2 = save(createProduct2());
            menu1 = save(createMenu1(menuGroup1, Collections.singletonList(product1)));
            orderTable = save(new OrderTable(3, false));
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

            assertDoesNotThrow(() -> orderService.create(request));
        }
    }
//
//    @DisplayName("주문 상태 변경은")
//    @Nested
//    class ChangeStatus {
//
//        private final Long orderId = 1L;
//        private Order order;
//        private OrderLineItem orderLineItem1;
//        private OrderLineItem orderLineItem2;
//
//        @BeforeEach
//        void setUp() {
//            order = createOrder(orderId);
//            orderLineItem1 = createOrderLineItem();
//            orderLineItem2 = createOrderLineItem();
//        }
//
//        private void subject() {
//            orderService.changeOrderStatus(orderId, order);
//        }
//
//        @DisplayName("존재하지 않는 주문일 경우 변경할 수 없다.")
//        @Test
//        void changeOrderStatusExceptionIfNotExist() {
//            when(orderDao.findById(any())).thenReturn(Optional.empty());
//
//            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("계산 완료된 주문일 경우 변경할 수 없다.")
//        @Test
//        void changeOrderStatusExceptionIfCompletion() {
//            order.setOrderStatus(OrderStatus.COMPLETION.name());
//            when(orderDao.findById(any())).thenReturn(Optional.of(order));
//
//            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("조건을 만족할 경우 변경할 수 있다.")
//        @Test
//        void changeOrderStatus() {
//            when(orderDao.findById(any())).thenReturn(Optional.of(order));
//            when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));
//
//            assertDoesNotThrow(this::subject);
//        }
//    }
}
