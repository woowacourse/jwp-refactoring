package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.DomainFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("Order 등록 성공")
    @Test
    void create() {
        OrderLineItem orderLineItem = DomainFactory.createOrderLineItem(1L, null, 1L);
        Order order = DomainFactory.createOrder(null, 1L, Arrays.asList(orderLineItem));
        OrderTable orderTable = DomainFactory.createOrderTable(1, 1L, false);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        Order expectOrder = DomainFactory.createOrder(OrderStatus.COOKING.name(), 1L, Arrays.asList(orderLineItem));
        expectOrder.setId(1L);
        given(orderDao.save(order)).willReturn(expectOrder);
        OrderLineItem expectOrderLineItem = DomainFactory.createOrderLineItem(1L, expectOrder.getId(), 1L);
        expectOrderLineItem.setSeq(1L);
        given(orderLineItemDao.save(orderLineItem)).willReturn(expectOrderLineItem);

        Order actual = orderService.create(order);

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(expectOrder.getId());
            assertThat(actual.getOrderTableId()).isEqualTo(1L);
            assertThat(actual.getOrderStatus()).isEqualTo(expectOrder.getOrderStatus());
            assertThat(actual.getOrderLineItems().get(0).getOrderId()).isEqualTo(expectOrder.getId());
        });
    }

    @DisplayName("Order에 속하는 OrderLineItem이 아무것도 없는 경우 예외 반환")
    @Test
    void createEmptyOrderLineItem() {
        Order order = DomainFactory.createOrder(null, 1L, null);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order에 속하는 OrderLineItem 개수와 Menu 개수가 일치하지 않을 때 예외 반환")
    @Test
    void createNotMatchOrderLineItemCountAndMenuCount() {
        OrderLineItem orderLineItem = DomainFactory.createOrderLineItem(1L, 1L, 1L);
        Order order = DomainFactory.createOrder(null, 1L, Arrays.asList(orderLineItem));
        given(menuDao.countByIdIn(anyList())).willReturn(2L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order를 받은 OrderTable이 비어있는 경우 예외 반환")
    @Test
    void createEmptyOrderTable() {
        OrderLineItem orderLineItem = DomainFactory.createOrderLineItem(1L, 1L, 1L);
        Order order = DomainFactory.createOrder(null, 1L, Arrays.asList(orderLineItem));
        OrderTable orderTable = DomainFactory.createOrderTable(1, 1L, true);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }
}
