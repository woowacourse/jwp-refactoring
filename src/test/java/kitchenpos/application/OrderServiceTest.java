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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.testutils.TestDomainBuilder.orderBuilder;
import static kitchenpos.testutils.TestDomainBuilder.orderLineItemBuilder;
import static kitchenpos.testutils.TestDomainBuilder.orderTableBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@MockitoSettings
class OrderServiceTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

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

    private OrderTable orderTable, emptyTable;
    private Long orderTableId;

    @BeforeEach
    void setUp() {
        orderTable = orderTableBuilder()
                .id(1L)
                .numberOfGuests(0)
                .empty(false)
                .build();
        emptyTable = orderTableBuilder()
                .id(2L)
                .numberOfGuests(0)
                .empty(true)
                .build();
        orderTableId = orderTable.getId();
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        OrderLineItem newOrderLineItem1 = orderLineItemBuilder()
                .menuId(1L)
                .quantity(1L)
                .build();
        OrderLineItem newOrderLineItem2 = orderLineItemBuilder()
                .menuId(2L)
                .quantity(2L)
                .build();

        Order newOrder = orderBuilder()
                .orderTableId(orderTableId)
                .orderLineItems(Arrays.asList(newOrderLineItem1, newOrderLineItem2))
                .build();

        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.ofNullable(orderTable));
        given(orderDao.save(newOrder)).willReturn(newOrder);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(
                newOrderLineItem1, newOrderLineItem2
        );

        // when
        Order order = orderService.create(newOrder);

        // then
        assertThat(order.getOrderTableId()).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(order.getOrderedTime()).isNotNull();
        assertThat(order.getOrderLineItems()).isNotNull();

        then(menuDao).should(times(1)).countByIdIn(anyList());
        then(orderTableDao).should(times(1)).findById(orderTableId);
        then(orderDao).should(times(1)).save(newOrder);
        then(orderLineItemDao).should(times(2)).save(any(OrderLineItem.class));
    }

    @DisplayName("주문을 생성에 실패한다 - 주문 항목은 하나라도 존재해야 한다.")
    @Test
    void createWithEmptyOrderLineItems() {
        // given
        Order newOrder = orderBuilder()
                .orderTableId(orderTableId)
                .orderLineItems(Collections.emptyList())
                .build();

        // when, then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
        then(orderLineItemDao).should(never()).save(any());
    }

    @DisplayName("주문을 생성에 실패한다 - 주문 항목 리스트에 존재하지 않는 메뉴가 있다.")
    @Test
    void createWithNonexistentMenu() {
        // given
        OrderLineItem newOrderLineItem = orderLineItemBuilder()
                .menuId(NON_EXISTENT_ID)
                .quantity(1L)
                .build();
        Order newOrder = orderBuilder()
                .orderTableId(orderTableId)
                .orderLineItems(Collections.singletonList(newOrderLineItem))
                .build();

        given(menuDao.countByIdIn(anyList())).willReturn(0L);

        // when, then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
        then(orderLineItemDao).should(never()).save(any());
    }

    @DisplayName("주문을 생성에 실패한다 - 주문 항목 리스트에 중복되는 메뉴가 있다.")
    @Test
    void createWithDuplicatedMenu() {
        // given
        OrderLineItem newOrderLineItem = orderLineItemBuilder()
                .menuId(1L)
                .quantity(1L)
                .build();
        Order newOrder = orderBuilder()
                .orderTableId(orderTableId)
                .orderLineItems(Arrays.asList(newOrderLineItem, newOrderLineItem))
                .build();

        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        // when, then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
        then(orderLineItemDao).should(never()).save(any());
    }

    @DisplayName("주문을 생성에 실패한다 - 주문 테이블이 존재하지 않는다.")
    @Test
    void createWithNonexistentOrderTable() {
        // given
        OrderLineItem newOrderLineItem = orderLineItemBuilder()
                .menuId(1L)
                .quantity(1L)
                .build();
        Order newOrder = orderBuilder()
                .orderTableId(NON_EXISTENT_ID)
                .orderLineItems(Collections.singletonList(newOrderLineItem))
                .build();

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
        then(orderLineItemDao).should(never()).save(any());
    }

    @DisplayName("주문을 생성에 실패한다 - 주문 테이블이 빈 테이블이다.")
    @Test
    void createWithEmptyTable() {
        // given
        OrderLineItem newOrderLineItem = orderLineItemBuilder()
                .menuId(1L)
                .quantity(1L)
                .build();
        Order newOrder = orderBuilder()
                .orderTableId(emptyTable.getId())
                .orderLineItems(Collections.singletonList(newOrderLineItem))
                .build();

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(emptyTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
        then(orderLineItemDao).should(never()).save(any());
    }

    @DisplayName("전체 주문의 리스트를 가져온다.")
    @Test
    void list() {
        // given
        Order order1 = orderBuilder()
                .id(1L)
                .build();
        Order order2 = orderBuilder()
                .id(2L)
                .build();
        OrderLineItem orderLineItem = orderLineItemBuilder()
                .build();


        given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2));
        given(orderLineItemDao.findAllByOrderId(anyLong()))
                .willReturn(Collections.singletonList(orderLineItem));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).extracting(Order::getOrderLineItems).isNotEmpty();

        then(orderDao).should(times(1)).findAll();
        then(orderLineItemDao).should(times(2)).findAllByOrderId(anyLong());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatusCookingToMeal() {
        // given
        Long orderId = 1L;
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order newOrder = orderBuilder().orderStatus(orderStatus.name()).build();

        Order savedOrder = orderBuilder()
                .id(orderId)
                .orderStatus(OrderStatus.COOKING.name())
                .build();
        OrderLineItem savedOrderLineItem = orderLineItemBuilder()
                .orderId(orderId)
                .build();

        given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));
        given(orderLineItemDao.findAllByOrderId(orderId)).willReturn(
                Collections.singletonList(savedOrderLineItem)
        );

        // when
        Order order = orderService.changeOrderStatus(orderId, newOrder);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        assertThat(order.getOrderLineItems()).isNotNull();

        then(orderDao).should(times(1)).findById(orderId);
        then(orderDao).should(times(1)).save(savedOrder);
        then(orderLineItemDao).should(times(1)).findAllByOrderId(orderId);
    }

    @DisplayName("주문 상태 변경에 실패한다 - 존재하지 않는 주문")
    @Test
    void changeOrderStatusWhenNonexistentOrder() {
        // given
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order newOrder = orderBuilder().orderStatus(orderStatus.name()).build();

        given(orderDao.findById(NON_EXISTENT_ID)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(NON_EXISTENT_ID, newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
    }

    @DisplayName("주문 상태 변경에 실패한다 - 이미 완료된 주문")
    @Test
    void changeOrderStatusWhenCompletesOrder() {
        // given
        Long orderId = 1L;
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order newOrder = orderBuilder().orderStatus(orderStatus.name()).build();

        Order savedOrder = orderBuilder()
                .id(orderId)
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();

        given(orderDao.findById(orderId)).willReturn(Optional.of(savedOrder));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, newOrder))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderDao).should(never()).save(any());
    }
}
