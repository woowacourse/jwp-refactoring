package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.domain.*;
import kitchenpos.dto.request.order.ChangeOrderStatusRequest;
import kitchenpos.dto.request.order.CreateOrderRequest;
import kitchenpos.dto.request.order.OrderLineItemRequest;
import kitchenpos.dto.response.CreateOrderResponse;
import kitchenpos.dto.response.OrderResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.fixture.MenuFixture.후라이드_단품;
import static kitchenpos.fixture.OrderFixture.COMPLETION_ORDER;
import static kitchenpos.fixture.OrderFixture.COOKING_ORDER;
import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블1;
import static kitchenpos.fixture.OrderTableFixture.단일_손님2_테이블;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("OrderService 단위 테스트")
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

    @Test
    @DisplayName("주문을 등록할 수 있다. - 해당 주문은 조리중(COOKING) 상태가 된다.")
    void create() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(
                단일_손님2_테이블.getId(),
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        Order order = new Order(
                1L,
                단일_손님2_테이블,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(후라이드_단품, 2))
        );

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(단일_손님2_테이블));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(후라이드_단품));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        CreateOrderResponse actual = orderService.create(request);

        // then
        assertEquals(OrderStatus.COOKING, actual.getOrderStatus());
    }

    @Test
    @DisplayName("메뉴 목록은 하나이상 있어야한다.")
    void createWrongOrderLineItemsEmpty() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(
                단일_손님2_테이블.getId(),
                Collections.emptyList()
        );
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(단일_손님2_테이블));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(request));
        assertEquals("주문하려면 하나 이상의 메뉴가 필요합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("메뉴 목록에 포함된 메뉴들은 모두 등록된 메뉴여야한다.")
    void createWrongOrderLineItemsNotRegister() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(
                단일_손님2_테이블.getId(),
                Collections.singletonList(new OrderLineItemRequest(10L, 2))
        );
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(단일_손님2_테이블));
        given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(request));
        assertEquals("등록되지 않은 메뉴는 주문할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문하려는 테이블은 존재해야 한다.")
    void createWrongTableNotExist() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(
                10L,
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(request));
        assertEquals("존재하지 않는 테이블은 주문할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문하려는 테이블은 비어있지 않아야한다.")
    void createWrongTableEmpty() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(
                단일_손님0_테이블1.getId(),
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(단일_손님0_테이블1));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(후라이드_단품));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.create(request));
        assertEquals("빈 테이블은 주문할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void list() {
        // given
        given(orderRepository.findAll()).willReturn(Arrays.asList(COOKING_ORDER, COMPLETION_ORDER));

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(COOKING_ORDER));

        // when
        OrderResponse actual = orderService.changeOrderStatus(COOKING_ORDER.getId(), request);

        // then
        assertEquals(OrderStatus.MEAL, actual.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문은 존재해야 한다.")
    void changeOrderStatusWrongOrderNotExist() {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(10L, request));
        assertEquals("존재하지 않는 주문의 상태는 변경할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문 상태는 조리중(COOKING)이나 식사중(MEAL)이어야한다.")
    void changeOrderStatusWrongOrderStatus() {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(COMPLETION_ORDER));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.changeOrderStatus(COMPLETION_ORDER.getId(), request));
        assertEquals("계산 완료된 주문의 상태는 변경할 수 없습니다.", exception.getMessage());
    }
}
