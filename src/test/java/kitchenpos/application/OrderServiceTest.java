package kitchenpos.application;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        Order order = mock(Order.class);
        when(orderDao.findAll()).thenReturn(Collections.singletonList(order));
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(Collections.singletonList(orderLineItem));

        // when
        List<Order> result = orderService.list();

        // then
        verify(orderDao, times(1)).findAll();
        verify(orderLineItemDao, times(1)).findAllByOrderId(order.getId());
        assertThat(result).containsExactly(order);
    }

    @Nested
    class CreateTest {

        MenuProduct menuProduct;
        Menu menu;
        List<Long> menuIds;
        OrderLineItem orderLineItem;
        OrderTable orderTable;
        Order order;
        Order savedOrder;

        @BeforeEach
        void setUp() {
            menuProduct = MenuProductFactory.builder()
                    .productId(1L)
                    .quantity(1L)
                    .build();

            menu = MenuFactory.builder()
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
            when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));
            when(orderDao.save(order)).thenReturn(savedOrder);
            when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);

            // when
            Order result = orderService.create(order);

            // then
            verify(menuDao, times(1)).countByIdIn(menuIds);
            verify(orderTableDao, times(1)).findById(order.getOrderTableId());
            verify(orderDao, times(1)).save(order);
            verify(orderLineItemDao, times(1)).save(orderLineItem);
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

            // when // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 생성 실패한다 - orderLineItem 개수가 menu 수와 일치하지 않는 경우")
        @Test
        void createFail_whenOrderLineItemCountIsNotEqualToMenuCount() {
            // given
            when(menuDao.countByIdIn(menuIds)).thenReturn(0L);

            // when // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(menuDao, times(1)).countByIdIn(menuIds);
        }

        @DisplayName("Order 생성 실패한다 - orderTable 이 존재하지 않는 경우")
        @Test
        void createFail_whenOrderTableDoesNotExist() {
            // given
            when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.empty());

            // when // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(menuDao, times(1)).countByIdIn(menuIds);
            verify(orderTableDao, times(1)).findById(order.getOrderTableId());
        }

        @DisplayName("Order 생성 실패한다 - orderTable 이 비어있는 경우")
        @Test
        void createFail_whenOrderTableIsEmpty() {
            // given
            orderTable = OrderTableFactory.copy(orderTable)
                    .empty(true)
                    .build();
            when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
            when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

            // when // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
            verify(menuDao, times(1)).countByIdIn(menuIds);
            verify(orderTableDao, times(1)).findById(order.getOrderTableId());
        }
    }

    @Nested
    class ChangeTest {

        Long orderId;

        List<OrderLineItem> orderLineItems;

        Order savedOrder;

        Order order;

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
            when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));
            when(orderLineItemDao.findAllByOrderId(orderId)).thenReturn(orderLineItems);

            // when
            Order result = orderService.changeOrderStatus(orderId, order);

            // then
            verify(orderDao, times(1)).findById(orderId);
            verify(orderDao, times(1)).save(savedOrder);
            verify(orderLineItemDao, times(1)).findAllByOrderId(orderId);
            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(order);
        }

        @DisplayName("Order 상태 변경 실패한다 - orderId 에 대한 order 가 존재하지 않는 경우")
        @Test
        void changeOrderStatusFail_whenOrderDoesNotExist() {
            // given
            when(orderDao.findById(orderId)).thenReturn(Optional.empty());

            // when // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Order 상태 변경 실패한다 - order 가 이미 완료된 order 인 경우")
        @Test
        void changeOrderStatusFail_whenOrderIsAlreadyCompleted() {
            // given
            savedOrder = OrderFactory.copy(savedOrder)
                    .orderStatus(OrderStatus.COMPLETION.name())
                    .build();
            when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));

            // when // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
