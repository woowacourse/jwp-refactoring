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
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;

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

    List<OrderLineItem> lineItems;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        OrderLineItem orderLineItem1 = OrderLineItemFixture.createWithoutId(1L, null);
        OrderLineItem orderLineItem2 = OrderLineItemFixture.createWithoutId(2L, null);
        OrderLineItem orderLineItem3 = OrderLineItemFixture.createWithoutId(3L, null);

        lineItems = Arrays.asList(orderLineItem1, orderLineItem2, orderLineItem3);
    }

    @DisplayName("정상적으로 Order를 생성한다")
    @Test
    void create() {
        Order order = OrderFixture.createWithoutId(OrderStatus.MEAL.name(), 1L, lineItems);
        Order orderWithId = OrderFixture.createWithId(1L, OrderStatus.MEAL.name(), 1L, lineItems);
        OrderTable notEmptyTable = OrderTableFixture.createNotEmptyWithId(1L);

        when(menuDao.countByIdIn(anyList())).thenReturn(Long.valueOf(lineItems.size()));
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(notEmptyTable));
        when(orderDao.save(order)).thenReturn(orderWithId);
        when(orderLineItemDao.save(any())).then(AdditionalAnswers.returnsFirstArg());

        Order savedOrder = orderService.create(order);
        assertThat(savedOrder).isEqualToComparingFieldByField(orderWithId);
        assertThat(savedOrder.getOrderLineItems())
            .extracting(OrderLineItem::getOrderId)
            .allMatch(id -> id.equals(orderWithId.getId()));
    }

    @DisplayName("OrderItem을 포함하지 않으면 Order 생성 요청 시 예외를 반환한다")
    @Test
    void createWithoutOrderItem() {
        Order orderWithoutItem = OrderFixture.createWithoutId(OrderStatus.MEAL.name(), 1L,
            Lists.emptyList());

        assertThatThrownBy(() -> orderService.create(orderWithoutItem))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu가 존재하지 않으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistMenu() {
        Order orderWithNotExistMenu = OrderFixture.createWithoutId(OrderFixture.MEAL_STATUS, 1L,
            lineItems);

        when(menuDao.countByIdIn(anyList())).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(orderWithNotExistMenu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Table이 존재하지 않으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        Order orderWithNotExistTable = OrderFixture.createWithoutId(OrderFixture.MEAL_STATUS, 1L,
            lineItems);
        when(menuDao.countByIdIn(anyList()))
            .thenReturn(Long.valueOf(orderWithNotExistTable.getOrderLineItems().size()));
        when(orderTableDao.findById(orderWithNotExistTable.getOrderTableId())).thenReturn(
            Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderWithNotExistTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order Table이 비어있으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        Order orderWithEmptyTable
            = OrderFixture.createWithoutId(OrderFixture.MEAL_STATUS, 1L, lineItems);
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);

        when(menuDao.countByIdIn(anyList())).thenReturn(
            Long.valueOf(orderWithEmptyTable.getOrderLineItems().size()));
        when(orderTableDao.findById(orderWithEmptyTable.getOrderTableId())).thenReturn(
            Optional.of(emptyTable));

        assertThatThrownBy(() -> orderService.create(orderWithEmptyTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 Order를 모두 조회한다.")
    @Test
    void list() {
        Order order1 = OrderFixture.createWithId(1L, OrderFixture.MEAL_STATUS, 1L, lineItems);
        Order order2 = OrderFixture.createWithId(2L, OrderFixture.MEAL_STATUS, 1L, lineItems);
        when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(lineItems);

        assertThat(orderService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(order1, order2));
    }

    @DisplayName("정상적으로 저장된 Order의 Status를 수정한다.")
    @Test
    void changeOrderStatus() {
        Order mealOrder = OrderFixture.createWithId(1L, OrderFixture.MEAL_STATUS, 1L, lineItems);
        when(orderDao.findById(mealOrder.getId())).thenReturn(Optional.of(mealOrder));

        Order cookingOrder = OrderFixture.createWithId(1L, OrderFixture.COOKING_STATUS, 1L,
            lineItems);

        assertThat(orderService.changeOrderStatus(mealOrder.getId(), cookingOrder))
            .extracting(Order::getOrderStatus)
            .isEqualTo(cookingOrder.getOrderStatus());
    }

    @DisplayName("이미 완료된 Order의 Status를 수정 요청 시 예외를 반환한다")
    @Test
    void changeCompletionOrderStatus() {
        Order completeOrder = OrderFixture.createWithId(1L, OrderFixture.COMPLETION, 1L, lineItems);
        Order cookingOrder = OrderFixture.createWithId(1L, OrderFixture.COOKING_STATUS, 1L,
            lineItems);

        when(orderDao.findById(completeOrder.getId())).thenReturn(Optional.of(completeOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(completeOrder.getId(), cookingOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
