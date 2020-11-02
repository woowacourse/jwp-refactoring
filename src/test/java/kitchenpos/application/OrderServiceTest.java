package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
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

    @Test
    @DisplayName("주문 생성")
    void create() {
        OrderLineItem orderLineItem = createOrderLineItem(null, null, 2L, 0);
        Order order = createOrder(1L, 3L, null, null, Collections.singletonList(orderLineItem));
        OrderTable orderTable = createOrderTable(null, null, 0, false);
        Order saved = createOrder(1L, 3L, OrderStatus.COOKING.name(), null, Collections.emptyList());

        given(menuDao.countByIdIn(Collections.singletonList(2L))).willReturn(1L);
        given(orderTableDao.findById(3L)).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).willReturn(saved);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(orderLineItem);

        Order result = orderService.create(order);

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(1L),
            () -> assertThat(result.getOrderTableId()).isEqualTo(3L),
            () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("주문 목록 조회")
    void list() {
        OrderLineItem orderLineItem = createOrderLineItem(null, 1L, 2L, 0);
        Order order = createOrder(1L, 3L, null, null, Collections.singletonList(orderLineItem));

        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(Collections.singletonList(orderLineItem));

        List<Order> result = orderService.list();

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0)).usingRecursiveComparison()
                .isEqualTo(order)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("수량이 있는 메뉴가 null이거나 존재하지 않는 경우 예외 발생")
    void orderLineItemDoesNotExist(List<OrderLineItem> input) {
        Order order = createOrder(null, null, null, null, input);
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문에 포함된 메뉴와 등록된 메뉴에서 조회한 것이 다른 경우 예외 발생")
    void differentCountOfOrderLineItemAndMenu() {
        OrderLineItem orderLineItem = createOrderLineItem(null, null, 2L, 0);
        Order order = createOrder(1L, null, null, null, Collections.singletonList(orderLineItem));

        given(menuDao.countByIdIn(Collections.singletonList(2L))).willReturn(0L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 경우 예외 발생")
    void emptyOrderTable() {
        OrderLineItem orderLineItem = createOrderLineItem(null, null, 2L, 0);
        Order order = createOrder(1L, 3L, null, null, Collections.singletonList(orderLineItem));
        OrderTable orderTable = createOrderTable(null, null, 0, true);
        orderTable.setEmpty(true);

        given(menuDao.countByIdIn(Collections.singletonList(2L))).willReturn(1L);
        given(orderTableDao.findById(3L)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
