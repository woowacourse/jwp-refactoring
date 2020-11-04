package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private Order order1;
    private Order order2;
    private Order order3;

    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private OrderLineItem orderLineItem3;

    private OrderTable notEmptyTable;
    private OrderTable emptyTable;

    List<OrderLineItem> lineItems;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setOrderId(null);

        orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setOrderId(null);

        orderLineItem3 = new OrderLineItem();
        orderLineItem3.setMenuId(2L);
        orderLineItem3.setOrderId(null);

        lineItems = Arrays.asList(orderLineItem1, orderLineItem2, orderLineItem3);

        notEmptyTable = new OrderTable();
        notEmptyTable.setId(1L);
        notEmptyTable.setEmpty(false);
        notEmptyTable.setNumberOfGuests(3);
        notEmptyTable.setTableGroupId(null);

        emptyTable = new OrderTable();
        emptyTable.setId(1L);
        emptyTable.setEmpty(true);
        emptyTable.setNumberOfGuests(3);
        emptyTable.setTableGroupId(null);

        order1 = new Order();
        order1.setOrderStatus(OrderStatus.MEAL.name());
        order1.setOrderTableId(1L);
        order1.setOrderLineItems(lineItems);

        order2 = new Order();
        order2.setId(1L);
        order2.setOrderStatus(OrderStatus.MEAL.name());
        order2.setOrderTableId(1L);
        order2.setOrderLineItems(lineItems);

        order3 = new Order();
        order3.setId(2L);
        order3.setOrderStatus(OrderStatus.COMPLETION.name());
        order3.setOrderTableId(2L);
        order3.setOrderLineItems(lineItems);
    }

    @DisplayName("정상적으로 Order를 생성한다")
    @Test
    void create() {
        when(menuDao.countByIdIn(anyList())).thenReturn(Long.valueOf(lineItems.size()));
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(notEmptyTable));
        when(orderDao.save(order1)).thenReturn(order2);
        when(orderLineItemDao.save(any())).then(AdditionalAnswers.returnsFirstArg());

        Order savedOrder = orderService.create(order1);
        assertThat(savedOrder).isEqualToComparingFieldByField(order2);
        assertThat(savedOrder.getOrderLineItems())
            .extracting(OrderLineItem::getOrderId)
            .allMatch(id -> id.equals(order2.getId()));
    }

    @DisplayName("OrderItem이 없는 Order를 생성 요청 시 예외를 반환한다")
    @Test
    void createWithoutOrderItem() {
        order1.setOrderLineItems(Lists.emptyList());
        assertThatThrownBy(() -> orderService.create(order1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되어있지 않은 Menu를 포함한 OrderItem을 포함한 Order를 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistMenu() {
        when(menuDao.countByIdIn(anyList())).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(order1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장되어있지 않은 Table Id를 갖는 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        when(menuDao.countByIdIn(anyList())).thenReturn(Long.valueOf(order1.getOrderLineItems().size()));
        when(orderTableDao.findById(order1.getOrderTableId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 테이블에서 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        when(menuDao.countByIdIn(anyList())).thenReturn(Long.valueOf(order1.getOrderLineItems().size()));
        when(orderTableDao.findById(order1.getOrderTableId())).thenReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> orderService.create(order1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Order를 모두 가져온다.")
    @Test
    void list() {
        when(orderDao.findAll()).thenReturn(Arrays.asList(order2, order3));
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(lineItems);

        assertThat(orderService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(order2, order3));
    }

    @DisplayName("정상적으로 저장된 Order의 Status를 수정한다.")
    @Test
    void changeOrderStatus() {
        when(orderDao.findById(order2.getId())).thenReturn(Optional.of(order2));

        Order newOrderStatus = new Order();
        newOrderStatus.setOrderStatus(OrderStatus.COMPLETION.name());

        assertThat(orderService.changeOrderStatus(order2.getId(), newOrderStatus))
            .extracting(Order::getOrderStatus)
            .isEqualTo(newOrderStatus.getOrderStatus());
    }

    @DisplayName("이미 완료된 Order의 Status를 수정 요청 시 예외를 반환한다")
    @Test
    void changeCompletionOrderStatus() {
        when(orderDao.findById(order3.getId())).thenReturn(Optional.of(order3));

        Order newOrderStatus = new Order();
        newOrderStatus.setOrderStatus(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order3.getId(), newOrderStatus))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
