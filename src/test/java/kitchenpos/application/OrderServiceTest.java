package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("id가 없는 주문으로 id가 있는 주문을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 1L, 2);
        final List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        final Order orderWithoutId = createOrder(null, 1L, OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);
        final Order expectedOrder = createOrder(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);
        final OrderTable orderTable = createOrderTable(1L, 1L, 2, false);

        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(expectedOrder.getOrderLineItems().size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any(Order.class))).willReturn(expectedOrder);
        final Order persistOrder = orderService.create(orderWithoutId);

        assertThat(persistOrder).usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(expectedOrder);
    }

    @DisplayName("주문 항목과 존재하는 메뉴의 크기가 다를 경우 예외를 반환한다.")
    @Test
    void createTest3() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 2L, 0);
        final Order invalidOrder = createOrder(1L, 1L, OrderStatus.MEAL, LocalDateTime.now(),
            Collections.singletonList(orderLineItem));

        given(menuRepository.countByIdIn(anyList())).willReturn(
            Long.valueOf(invalidOrder.getOrderLineItems().size() + 1));

        assertThatThrownBy(() -> orderService.create(invalidOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 테이블이 없으면 예외를 반환한다.")
    @Test
    void createTest4() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 2L, 0);
        final Order invalidOrder = createOrder(1L, Long.MAX_VALUE, OrderStatus.MEAL, LocalDateTime.now(),
            Collections.singletonList(orderLineItem));

        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(invalidOrder.getOrderLineItems().size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(invalidOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 테이블이 비어있으면 예외를 반환한다.")
    @Test
    void createTest5() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 2L, 0);
        final Order invalidOrder = createOrder(1L, 1L, OrderStatus.MEAL, LocalDateTime.now(),
            Collections.singletonList(orderLineItem));
        final OrderTable emptyOrderTable = createOrderTable(1L, 1L, 0, true);

        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(invalidOrder.getOrderLineItems().size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(emptyOrderTable));

        assertThatThrownBy(() -> orderService.create(invalidOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 1L, 2);
        final List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        final Order expectedOrder = createOrder(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);
        final List<Order> expectedOrders = Collections.singletonList(expectedOrder);

        given(orderRepository.findAll()).willReturn(expectedOrders);

        final List<Order> persistOrders = orderService.list();

        assertThat(persistOrders).usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(expectedOrders);
    }

    @DisplayName("주문의 상태를 정상적으로 수정한다.")
    @Test
    void changeOrderStatusTest() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 1L, 2);
        final List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        final Order requestOrder = createOrder(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(),
            orderLineItems);
        final Order savedOrder = createOrder(1L, 1L, OrderStatus.MEAL, LocalDateTime.now(), orderLineItems);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(savedOrder));
        given(orderRepository.save(any(Order.class))).willReturn(requestOrder);

        assertThat(orderService.changeOrderStatus(1L, requestOrder))
            .usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(requestOrder);
    }

    @DisplayName("없는 주문의 상태 변경시 예외를 반환한다.")
    @Test
    void changeOrderStatusTest2() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, Long.MAX_VALUE, 1L, 2);
        final List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        final Order requestOrder = createOrder(Long.MAX_VALUE, 1L, OrderStatus.COMPLETION, LocalDateTime.now(),
            orderLineItems);

        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(Long.MAX_VALUE, requestOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 계산 완료된 주문의 상태 변경시 예외를 반환한다.")
    @Test
    void changeOrderStatusTest3() {
        final OrderLineItem orderLineItem = createOrderLineItem(1L, 1L, 1L, 2);
        final List<OrderLineItem> orderLineItems = Collections.singletonList(orderLineItem);
        final Order savedOrder = createOrder(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(),
            orderLineItems);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(savedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, savedOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹내의 주문 테이블과 등록된 주문 테이블에서 조회한 것이 다르면 예외를 반환한다.")
    @Test
    void findAllByIdInTest() {
        final OrderTable firstTable = createOrderTable(1L, 3L, 0, true);
        final OrderTable secondTable = createOrderTable(2L, 3L, 0, true);
        final List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Collections.singletonList(firstTable));

        assertThatThrownBy(() -> orderService.findAllByIdIn(Arrays.asList(1L, 2L)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
