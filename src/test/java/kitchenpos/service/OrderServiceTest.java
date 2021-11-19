package kitchenpos.service;

import kitchenpos.application.OrderLineItemService;
import kitchenpos.application.OrderService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrdersRepository;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrdersRequest;
import kitchenpos.ui.dto.OrdersResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("주문 테스트")
class OrderServiceTest extends ServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderLineItemService orderLineItemService;

    private OrdersRequest ordersRequest;

    @BeforeEach
    void setUp() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(MenuFixture.create().getId(), 1L);

        ordersRequest = new OrdersRequest(OrderTableFixture.create().getId(), OrderStatus.MEAL, Collections.singletonList(orderLineItemRequest));
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        //given
        when(orderLineItemService.createEntity(any())).thenReturn(OrderLineItemFixture.create());
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(OrderTableFixture.create()));
        when(ordersRepository.save(any())).thenReturn(OrderFixture.create());
        when(orderLineItemService.saveAll(anyList(), any())).thenReturn(OrderLineItemResponse.of(OrderLineItemFixture.create()));
        //when
        OrdersResponse ordersResponse = orderService.create(ordersRequest);
        //then
        assertThat(ordersResponse.getId()).isNotNull();
    }

    @DisplayName("주문 조회")
    @Test
    void findAll() {
        //given
        when(ordersRepository.findAll()).thenReturn(Collections.singletonList(OrderFixture.create()));
        when(orderLineItemService.findAllByOrdersId(anyLong())).thenReturn(OrderLineItemResponse.of(OrderLineItemFixture.create()));
        //when
        List<OrdersResponse> allOrders = orderService.findAll();
        //then
        assertThat(allOrders).hasSize(1);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        //given
        when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(OrderFixture.create()));
        when(orderLineItemService.findAllByOrdersId(anyLong())).thenReturn(OrderLineItemResponse.of(OrderLineItemFixture.create()));
        //when
        OrdersResponse ordersResponse = orderService.changeOrderStatus(1L, ordersRequest);
        //then
        assertThat(ordersResponse.getStatus()).isEqualTo(OrderStatus.MEAL);
    }
}