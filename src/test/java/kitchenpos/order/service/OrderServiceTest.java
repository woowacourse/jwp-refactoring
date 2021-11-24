package kitchenpos.order.service;

import static kitchenpos.fixtures.OrderFixtures.createOrder;
import static kitchenpos.fixtures.OrderFixtures.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.ServiceTest;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.service.dto.ChangeOrderStatusRequest;
import kitchenpos.order.service.dto.OrderRequest;
import kitchenpos.order.service.dto.OrderResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class OrderServiceTest extends ServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderRequest request;

    @BeforeEach
    void setUp() {
        order = createOrder();
        request = createOrderRequest();
    }

    @Test
    void 주문을_생성한다() {
        given(menuRepository.findById(any())).willReturn(Optional.of(MenuFixtures.createMenu()));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(TableFixtures.createOrderTable(false)));
        given(orderRepository.save(any())).willReturn(order);
        given(orderLineItemRepository.saveAll(any())).willReturn(OrderFixtures.createOrderLineItems());

        assertDoesNotThrow(() -> orderService.create(request));
        verify(orderLineItemRepository, times(1)).saveAll(any());
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void 생성_시_주문_항목의_수와_메뉴의_수가_일치하지_않으면_예외를_반환한다() {
        given(menuRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.create(request));
    }

    @Test
    void 생성_시_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        given(menuRepository.findById(any())).willReturn(Optional.of(MenuFixtures.createMenu()));
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.create(request));
    }

    @Test
    void 생성_시_주문항목이_비어있으면_예외를_반환한다() {
        given(orderTableRepository.findById(any())).willReturn(Optional.of(TableFixtures.createOrderTable(false)));
        OrderRequest emptyItemRequest = new OrderRequest(1L, OrderStatus.COOKING, Collections.emptyList());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> orderService.create(emptyItemRequest));
        assertThat(exception.getMessage()).isEqualTo("주문 항목이 비어있습니다.");
    }

    @Test
    void 생성_시_주문_테이블이_빈_테이블이면_예외를_반환한다() {
        given(menuRepository.findById(any())).willReturn(Optional.of(MenuFixtures.createMenu()));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(TableFixtures.createOrderTable(true)));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.create(request));
        assertThat(exception.getMessage()).isEqualTo("빈 테이블입니다.");
    }

    @Test
    void 주문_리스트를_반환한다() {
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));

        List<OrderResponse> orders = assertDoesNotThrow(() -> orderService.list());
        orders.stream()
            .map(OrderResponse::getOrderLineItems)
            .forEach(lineItems -> assertThat(lineItems).isNotEmpty());
    }

    @Test
    void 주문_상태를_변경한다() {
        ChangeOrderStatusRequest completedOrder = new ChangeOrderStatusRequest(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderRepository.save(any())).willReturn(OrderFixtures.createOrder(OrderStatus.COMPLETION));

        OrderResponse changedOrder = assertDoesNotThrow(
            () -> orderService.changeOrderStatus(this.order.getId(), completedOrder)
        );
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 상태_변경_시_주문이_존재하지_않으면_예외를_반환한다() {
        ChangeOrderStatusRequest completedOrder = new ChangeOrderStatusRequest(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(
            NoSuchElementException.class,
            () -> orderService.changeOrderStatus(this.order.getId(), completedOrder)
        );
    }
}