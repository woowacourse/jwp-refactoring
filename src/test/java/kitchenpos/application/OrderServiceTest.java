package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.AlreadyCompleteOrderException;
import kitchenpos.exception.AlreadyEmptyTableException;
import kitchenpos.exception.EmptyMenuOrderException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository,
            orderLineItemRepository);
    }

    @DisplayName("정상적으로 Order를 생성한다")
    @Test
    void create() {
        OrderCreateRequest request = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1), OrderLineItemFixture.createRequest(2L, 1),
            OrderLineItemFixture.createRequest(3L, 1));
        Order orderWithId = OrderFixture.createWithId(1L, OrderStatus.MEAL.name(), 1L);
        OrderTable notEmptyTable = OrderTableFixture.createNotEmptyWithId(1L);

        when(menuRepository.countByIdIn(anyList())).thenReturn(
            Long.valueOf(request.getOrderLineItems().size()));
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(notEmptyTable));
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithId);

        OrderResponse response = orderService.create(request);
        assertThat(response).isEqualToComparingFieldByField(orderWithId);
    }

    @DisplayName("OrderItem을 포함하지 않으면 Order 생성 요청 시 예외를 반환한다")
    @Test
    void createWithoutOrderItem() {
        OrderCreateRequest createEmptyItemRequest = OrderFixture.createRequest(1L);

        assertThatThrownBy(() -> orderService.create(createEmptyItemRequest))
            .isInstanceOf(EmptyMenuOrderException.class);
    }

    @DisplayName("Menu가 존재하지 않으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistMenu() {
        OrderCreateRequest requestWithNotExistMenu = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1));

        when(menuRepository.countByIdIn(anyList())).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(requestWithNotExistMenu))
            .isInstanceOf(MenuNotFoundException.class);
    }

    @DisplayName("Table이 존재하지 않으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        OrderCreateRequest requestWithNotExistTable = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1));

        when(menuRepository.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableRepository.findById(requestWithNotExistTable.getOrderTableId())).thenReturn(
            Optional.empty());

        assertThatThrownBy(() -> orderService.create(requestWithNotExistTable))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("Order Table이 비어있으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        OrderCreateRequest requestWithEmptyTable = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1));
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);

        when(menuRepository.countByIdIn(anyList())).thenReturn(1L);
        when(orderTableRepository.findById(requestWithEmptyTable.getOrderTableId())).thenReturn(
            Optional.of(emptyTable));

        assertThatThrownBy(() -> orderService.create(requestWithEmptyTable))
            .isInstanceOf(AlreadyEmptyTableException.class);
    }

    @DisplayName("정상적으로 저장된 Order를 모두 조회한다.")
    @Test
    void list() {
        Order order1 = OrderFixture.createWithId(1L, OrderFixture.MEAL_STATUS, 1L);
        Order order2 = OrderFixture.createWithId(2L, OrderFixture.MEAL_STATUS, 1L);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        assertThat(orderService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(order1, order2));
    }

    @DisplayName("정상적으로 저장된 Order의 Status를 수정한다.")
    @Test
    void changeOrderStatus() {
        Order mealOrder = OrderFixture.createWithId(1L, OrderFixture.MEAL_STATUS, 1L);
        when(orderRepository.findById(mealOrder.getId())).thenReturn(Optional.of(mealOrder));

        Order cookingOrder = OrderFixture.createWithId(1L, OrderFixture.COOKING_STATUS, 1L);

        assertThat(orderService.changeOrderStatus(mealOrder.getId(), cookingOrder))
            .extracting(OrderResponse::getOrderStatus)
            .isEqualTo(cookingOrder.getOrderStatus());
    }

    @DisplayName("이미 완료된 Order의 Status를 수정 요청 시 예외를 반환한다")
    @Test
    void changeCompletionOrderStatus() {
        Order completeOrder = OrderFixture.createWithId(1L, OrderFixture.COMPLETION, 1L);
        Order cookingOrder = OrderFixture.createWithId(1L, OrderFixture.COOKING_STATUS, 1L);

        when(orderRepository.findById(completeOrder.getId())).thenReturn(
            Optional.of(completeOrder));

        assertThatThrownBy(
            () -> orderService.changeOrderStatus(completeOrder.getId(), cookingOrder))
            .isInstanceOf(AlreadyCompleteOrderException.class);
    }
}
