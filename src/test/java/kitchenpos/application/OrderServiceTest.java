package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menu.ProductResponse;
import kitchenpos.dto.order.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private static final Integer 테이블_사람_1명 = 1;
    private static final Long 테이블_ID_1 = 1L;
    private static final Boolean 테이블_비어있음 = true;
    private static final Boolean 테이블_비어있지않음 = false;
    private static final Long 주문_메뉴_ID_1 = 1L;
    private static final Long 주문_메뉴_ID_2 = 2L;
    private static final Long 주문_메뉴_개수_1개 = 1L;
    private static final Long 주문_메뉴_개수_2개 = 2L;
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final Long 메뉴_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final String 상품_후라이드_치킨 = "후라이드 치킨";
    private static final String 상품_코카콜라 = "코카콜라";
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.0");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.0");
    private static final Long 주문_ID_1 = 1L;
    private static final LocalDateTime 주문_시간 = LocalDateTime.now();
    private static final String 주문_상태_조리중 = "COOKING";
    private static final String 주문_상태_완료 = "COMPLETION";
    List<MenuResponse> menuResponses;
    MenuGroupResponse menuGroupResponse;
    List<ProductResponse> productResponses;

    private OrderService orderService;
    List<OrderLineItemRequest> orderLineItemRequests;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemService orderLineItemService;
    @Mock
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderTableRepository, orderLineItemService, menuService);

        menuGroupResponse = new MenuGroupResponse(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        productResponses = Arrays.asList(
                new ProductResponse(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원),
                new ProductResponse(상품_ID_2, 상품_코카콜라, 상품_가격_1000원)
        );
        menuResponses = Arrays.asList(
                new MenuResponse(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroupResponse, productResponses)
        );
        orderLineItemRequests = Arrays.asList(
                new OrderLineItemRequest(주문_메뉴_ID_1, 주문_메뉴_개수_1개),
                new OrderLineItemRequest(주문_메뉴_ID_2, 주문_메뉴_개수_2개)
        );
    }

    @DisplayName("Order 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        OrderTable orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(테이블_ID_1, orderLineItemRequests);

        when(orderTableRepository.findById(anyLong())).thenReturn(java.util.Optional.of(orderTable));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        doNothing().when(orderLineItemService).createOrderLineItems(any(Order.class), any(OrderLineItemRequests.class));
        when(menuService.findMenusByOrder(any(Order.class))).thenReturn(menuResponses);

        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        assertThat(orderResponse.getId()).isEqualTo(주문_ID_1);
        assertThat(orderResponse.getOrderedTime()).isEqualTo(주문_시간);
        assertThat(orderResponse.getOrderStatus()).isEqualTo(주문_상태_조리중);
        assertThat(orderResponse.getMenus())
                .hasSize(1)
                .extracting("name")
                .containsOnly(메뉴_이름_후라이드_치킨);
    }

    @DisplayName("예외 테스트 : Order 생성 중 잚못된 OrderTable을 보내면, 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTableItemsExceptionTest() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(테이블_ID_1, orderLineItemRequests);

        when(orderTableRepository.findById(테이블_ID_1)).thenThrow(new IllegalArgumentException("해당 테이블을 찾을 수 없습니다."));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : Order 생성 중 비어있는 OrderTable을 보내면, 예외가 발생한다.")
    @Test
    void createWithEmptyOrderTableItemsExceptionTest() {
        OrderTable invalidOrderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음, null);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(테이블_ID_1, orderLineItemRequests);

        when(orderTableRepository.findById(테이블_ID_1)).thenReturn(java.util.Optional.of(invalidOrderTable));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블이 비어있습니다.");
    }

    @DisplayName("Order 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        OrderTable orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<OrderResponse> foundOrders = orderService.list();
        OrderResponse foundOrder = foundOrders.get(0);

        assertThat(foundOrders).hasSize(1);
        assertThat(foundOrder.getId()).isEqualTo(주문_ID_1);
        assertThat(foundOrder.getOrderStatus()).isEqualTo(주문_상태_조리중);
        assertThat(foundOrder.getOrderedTime()).isEqualTo(주문_시간);
        assertThat(foundOrder.getOrderTableResponse().getId()).isEqualTo(테이블_ID_1);
    }

    @DisplayName("Order의 상태를 변경 시 올바르게 변경된다.")
    @Test
    void changeOrderStatusTest() {
        OrderTable orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(주문_상태_완료);

        when(orderRepository.findById(주문_ID_1)).thenReturn(java.util.Optional.of(order));

        OrderResponse changedOrderResponse = orderService.changeOrderStatus(주문_ID_1, orderStatusRequest);

        assertThat(changedOrderResponse.getId()).isEqualTo(주문_ID_1);
        assertThat(changedOrderResponse.getOrderedTime()).isEqualTo(주문_시간);
        assertThat(changedOrderResponse.getOrderStatus()).isEqualTo(주문_상태_완료);
    }

    @DisplayName("예외 테스트 : 잘못된 Order의 ID를 전달 시, 예외가 발생한다.")
    @Test
    void changeOrderStatusInvalidIdExceptionTest() {
        OrderTable orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COOKING, 주문_시간);
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(주문_상태_완료);

        when(orderRepository.findById(주문_ID_1)).thenThrow(new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문_ID_1, orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 주문을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : 완료된 Order의 상태를 변경 시, 예외가 발생한다.")
    @Test
    void changeOrderStatusCompletionExceptionTest() {
        OrderTable orderTable = new OrderTable(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음, null);
        Order order = new Order(주문_ID_1, orderTable, OrderStatus.COMPLETION, 주문_시간);
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(주문_상태_완료);

        when(orderRepository.findById(주문_ID_1)).thenReturn(java.util.Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문_ID_1, orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }
}
