package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.factory.MenuFactory;
import kitchenpos.factory.MenuProductFactory;
import kitchenpos.factory.OrderFactory;
import kitchenpos.factory.OrderLineItemFactory;
import kitchenpos.factory.OrderTableFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
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

    @DisplayName("모든 Order 를 조회한다")
    @Test
    void list() {
        // given
        OrderLineItem orderLineItem = OrderLineItemFactory.builder().build();
        Order order = OrderFactory.builder().build();
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(
            Collections.singletonList(orderLineItem));

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).containsExactly(order);
    }

    @Nested
    class CreateTest {

        private List<Long> menuIds;

        private OrderLineItem orderLineItem;

        private OrderTable orderTable;

        private Order order;

        private Order savedOrder;

        @BeforeEach
        void setUp() {
            MenuProduct menuProduct = MenuProductFactory.builder()
                .productId(1L)
                .quantity(1L)
                .build();

            Menu menu = MenuFactory.builder()
                .id(1L)
                .name("후라이드+후라이드")
                .price(new BigDecimal(19000))
                .menuGroupId(1L)
                .menuProducts(Collections.singletonList(menuProduct))
                .build();
            menuIds = Collections.singletonList(menu.getId());

            orderLineItem = OrderLineItemFactory.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build();

            orderTable = OrderTableFactory.builder()
                .numberOfGuests(2)
                .empty(false)
                .build();

            order = OrderFactory.builder()
                .orderTableId(orderTable.getId())
                .orderLineItems(Collections.singletonList(orderLineItem))
                .build();

            savedOrder = OrderFactory.copy(order)
                .id(1L)
                .orderStatus(OrderStatus.COOKING.name())
                .build();
        }

        @DisplayName("Order 를 생성한다")
        @Test
        void create() {
            // given
            given(menuDao.countByIdIn(menuIds)).willReturn(1L);
            given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));
            given(orderDao.save(order)).willReturn(savedOrder);
            given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

            // when
            Order result = orderService.create(order);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedOrder);
        }

        @DisplayName("Order 생성 실패한다 - orderLineItems 가 비어있는 경우")
        @Test
        void createFail_whenOrderLineItemDoesNotExist() {
            // given
            order = OrderFactory.copy(order)
                .orderLineItems(Collections.emptyList())
                .build();

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderLineItem 개수가 menu 수와 일치하지 않는 경우")
        @Test
        void createFail_whenOrderLineItemCountIsNotEqualToMenuCount() {
            // given
            given(menuDao.countByIdIn(menuIds)).willReturn(0L);

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderTable 이 존재하지 않는 경우")
        @Test
        void createFail_whenOrderTableDoesNotExist() {
            // given
            given(menuDao.countByIdIn(menuIds)).willReturn(1L);
            given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderTable 이 비어있는 경우")
        @Test
        void createFail_whenOrderTableIsEmpty() {
            // given
            orderTable = OrderTableFactory.copy(orderTable)
                .empty(true)
                .build();
            given(menuDao.countByIdIn(menuIds)).willReturn(1L);
            given(orderTableDao.findById(order.getOrderTableId()))
                .willReturn(Optional.of(orderTable));

            // when
            ThrowingCallable throwingCallable = () -> orderService.create(order);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ChangeTest {

        private Long orderId;

        private List<OrderLineItem> orderLineItems;

        private Order savedOrder;

        private Order order;

        @BeforeEach
        void setUp() {
            orderId = 1L;

            orderLineItems = Collections.singletonList(mock(OrderLineItem.class));

            savedOrder = OrderFactory.builder()
                .id(orderId)
                .orderTableId(1L)
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .orderLineItems(orderLineItems)
                .build();

            order = OrderFactory.copy(savedOrder)
                .orderStatus(OrderStatus.MEAL.name())
                .build();
        }

        @DisplayName("Order 의 상태를 변경한다")
        @Test
        void changeOrderStatus() {
            // given
            given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));
            given(orderLineItemDao.findAllByOrderId(orderId)).willReturn(orderLineItems);

            // when
            Order result = orderService.changeOrderStatus(orderId, order);

            // then
            verify(orderDao, times(1)).save(savedOrder);
            assertThat(result)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(order);
        }

        @DisplayName("Order 상태 변경 실패한다 - orderId 에 대한 order 가 존재하지 않는 경우")
        @Test
        void changeOrderStatusFail_whenOrderDoesNotExist() {
            // given
            given(orderDao.findById(orderId)).willReturn(Optional.empty());

            // when
            ThrowingCallable throwingCallable =
                () -> orderService.changeOrderStatus(orderId, order);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 상태 변경 실패한다 - order 가 이미 완료된 order 인 경우")
        @Test
        void changeOrderStatusFail_whenOrderIsAlreadyCompleted() {
            // given
            savedOrder = OrderFactory.copy(savedOrder)
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();
            given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));

            // when
            ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(orderId,
                order);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
