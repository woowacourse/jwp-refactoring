package kitchenpos.application;

import static kitchenpos.OrderFixture.*;
import static kitchenpos.OrderTableFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @DisplayName("id가 없는 주문으로 id가 있는 주문을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final Order expectedOrder = createOrderWithId(1L);
        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(expectedOrder.getOrderLineItems().size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(createOrderTableWithId(1L)));
        given(orderRepository.save(any(Order.class))).willReturn(createOrderWithId(1L));
        given(orderLineItemRepository.save(any(OrderLineItem.class))).willReturn(createOrderLineItemWithOrderId(1L));
        final Order persistOrder = orderService.create(createOrderWithoutId());

        assertThat(expectedOrder).usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(persistOrder);
    }

    @DisplayName("주문 항목이 없는 주문을 생성 시도할 경우 예외를 반환한다.")
    @Test
    void createTest2() {
        final Order noOrderLineItemsOrder = createNoOrderLineItemsOrder();

        assertThatThrownBy(() -> orderService.create(noOrderLineItemsOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목과 존재하는 메뉴의 크기가 다를 경우 예외를 반환한다.")
    @Test
    void createTest3() {
        final Order invalidOrder = createOrderWithoutId();
        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(invalidOrder.getOrderLineItems().size() + 1));

        assertThatThrownBy(() -> orderService.create(invalidOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 테이블이 없으면 예외를 반환한다.")
    @Test
    void createTest4() {
        final Order invalidOrder = createOrderWithId(-1L);
        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(invalidOrder.getOrderLineItems().size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(invalidOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("선택한 주문 테이블이 비어있으면 예외를 반환한다.")
    @Test
    void createTest5() {
        final Order invalidOrder = createOrderWithoutId();
        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(invalidOrder.getOrderLineItems().size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(createEmptyOrderTable()));

        assertThatThrownBy(() -> orderService.create(invalidOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<Order> expectedOrders = createOrders();
        given(orderRepository.findAll()).willReturn(createOrders());
        given(orderLineItemRepository.findAllByOrderId(1L)).willReturn(
            Arrays.asList(createOrderLineItemWithOrderId(1L), createOrderLineItemWithOrderId(1L)));
        given(orderLineItemRepository.findAllByOrderId(2L)).willReturn(
            Arrays.asList(createOrderLineItemWithOrderId(2L), createOrderLineItemWithOrderId(2L)));

        final List<Order> persistOrders = orderService.list();

        assertThat(expectedOrders).usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(persistOrders);
    }

    @DisplayName("주문의 상태를 정상적으로 수정한다.")
    @Test
    void changeOrderStatusTest() {
        final Order requestOrder = createOrderWithId(1L);
        requestOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(createOrderWithId(1L)));
        final Order savedOrder = createOrderWithId(1L);
        savedOrder.setOrderStatus(requestOrder.getOrderStatus());
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);
        given(orderLineItemRepository.findAllByOrderId(anyLong())).willReturn(
            Arrays.asList(createOrderLineItemWithOrderId(1L), createOrderLineItemWithOrderId(1L)));

        assertThat(orderService.changeOrderStatus(1L, requestOrder))
            .usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(savedOrder);
    }

    @DisplayName("없는 주문의 상태 변경시 예외를 반환한다.")
    @Test
    void changeOrderStatusTest2() {
        final Order requestOrder = createOrderWithId(1L);

        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, requestOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 계산 완료된 주문의 상태 변경시 예외를 반환한다.")
    @Test
    void changeOrderStatusTest3() {
        final Order requestOrder = createOrderWithId(1L);
        requestOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(createOrderWithId(1L)));
        final Order savedOrder = createOrderWithId(1L);
        savedOrder.setOrderStatus(requestOrder.getOrderStatus());
        given(orderRepository.save(any(Order.class))).willReturn(savedOrder);
        given(orderLineItemRepository.findAllByOrderId(anyLong())).willReturn(
            Arrays.asList(createOrderLineItemWithOrderId(1L), createOrderLineItemWithOrderId(1L)));

        assertThat(orderService.changeOrderStatus(1L, requestOrder))
            .usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(savedOrder);
    }
}
