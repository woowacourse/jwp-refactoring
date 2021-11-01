package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.OrderFixture.createOrder;
import static kitchenpos.OrderFixture.createOrderLineItem;
import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        Order order1 = createOrder();
        Order order2 = createOrder();
        when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> actual = orderService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(order1, order2)
        );
    }

    @DisplayName("주문 생성은")
    @Nested
    class Create {

        private Order order;
        private OrderLineItem orderLineItem1;
        private OrderLineItem orderLineItem2;

        @BeforeEach
        void setUp() {
            order = createOrder();
            orderLineItem1 = createOrderLineItem(1L);
            orderLineItem2 = createOrderLineItem(2L);
        }

        private void subject() {
            orderService.create(order);
        }

        @DisplayName("주문 항목이 없는 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotHasItem() {
            order.setOrderLineItems(Collections.emptyList());

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴를 포함한 주문 항목이 포함된 경우 생성할 수 없다.")
        @Test
        void createExceptionIfHasNotExistMenu() {
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            when(menuDao.countByIdIn(any())).thenReturn(1L);

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블에 속한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfHasNotExistOrder() {
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            when(menuDao.countByIdIn(any())).thenReturn(2L);
            when(orderTableDao.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("빈 테이블의 주문은 생성할 수 없다.")
        @Test
        void createExceptionIfEmptyOrderTable() {
            OrderTable orderTable = createOrderTable();
            orderTable.setEmpty(true);
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            when(menuDao.countByIdIn(any())).thenReturn(2L);
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
        @Test
        void create() {
            OrderTable orderTable = createOrderTable();
            order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
            when(menuDao.countByIdIn(any())).thenReturn(2L);
            when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
            when(orderDao.save(any())).thenReturn(order);
            when(orderLineItemDao.save(any())).thenReturn(createOrderLineItem());

            assertDoesNotThrow(this::subject);
        }
    }

    @DisplayName("주문 상태 변경은")
    @Nested
    class ChangeStatus {

        private final Long orderId = 1L;
        private Order order;
        private OrderLineItem orderLineItem1;
        private OrderLineItem orderLineItem2;

        @BeforeEach
        void setUp() {
            order = createOrder(orderId);
            orderLineItem1 = createOrderLineItem();
            orderLineItem2 = createOrderLineItem();
        }

        private void subject() {
            orderService.changeOrderStatus(orderId, order);
        }

        @DisplayName("존재하지 않는 주문일 경우 변경할 수 없다.")
        @Test
        void changeOrderStatusExceptionIfNotExist() {
            when(orderDao.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산 완료된 주문일 경우 변경할 수 없다.")
        @Test
        void changeOrderStatusExceptionIfCompletion() {
            order.setOrderStatus(OrderStatus.COMPLETION.name());
            when(orderDao.findById(any())).thenReturn(Optional.of(order));

            assertThatThrownBy(this::subject).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족할 경우 변경할 수 있다.")
        @Test
        void changeOrderStatus() {
            when(orderDao.findById(any())).thenReturn(Optional.of(order));
            when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));

            assertDoesNotThrow(this::subject);
        }
    }
}
