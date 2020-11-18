package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderChangeStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.AlreadyCompleteOrderException;
import kitchenpos.exception.AlreadyEmptyTableException;
import kitchenpos.exception.EmptyMenuOrderException;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@SpringBootTest
@Sql("classpath:truncate.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("정상적으로 Order를 생성한다")
    @Test
    void create() {
        OrderCreateRequest request = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1), OrderLineItemFixture.createRequest(2L, 1),
            OrderLineItemFixture.createRequest(3L, 1));
        Order orderWithId = OrderFixture.createWithId(1L, OrderFixture.COOKING_STATUS, 1L);
        OrderTable notEmptyTable = OrderTableFixture.createNotEmptyWithId(1L);

        Menu menu1 = MenuFixture.createWithoutId(1L, 15000L);
        Menu menu2 = MenuFixture.createWithoutId(1L, 15000L);
        Menu menu3 = MenuFixture.createWithoutId(1L, 15000L);
        menuRepository.saveAll(Arrays.asList(menu1, menu2, menu3));
        orderTableRepository.save(notEmptyTable);

        OrderResponse response = orderService.create(request);
        assertThat(response).isEqualToComparingFieldByField(OrderResponse.of(orderWithId));
    }

    @DisplayName("OrderItem을 포함하지 않으면 Order 생성 요청 시 예외를 반환한다")
    @Test
    void createWithoutOrderItem() {
        OrderCreateRequest createEmptyItemRequest = OrderFixture.createRequest(1L);

        assertThatThrownBy(() -> orderService.create(createEmptyItemRequest))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("Menu가 존재하지 않으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistMenu() {
        OrderCreateRequest requestWithNotExistMenu = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1));

        assertThatThrownBy(() -> orderService.create(requestWithNotExistMenu))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("Table이 존재하지 않으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithNotExistTable() {
        OrderCreateRequest requestWithNotExistTable = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1));

        menuRepository.save(MenuFixture.createWithoutId(1L, 15000L));

        assertThatThrownBy(() -> orderService.create(requestWithNotExistTable))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("Order Table이 비어있으면 Order 생성 요청 시 예외를 반환한다.")
    @Test
    void createWithEmptyTable() {
        OrderCreateRequest requestWithEmptyTable = OrderFixture.createRequest(1L,
            OrderLineItemFixture.createRequest(1L, 1));
        OrderTable emptyTable = OrderTableFixture.createEmptyWithId(1L);

        menuRepository.save(MenuFixture.createWithoutId(1L, 15000L));
        orderTableRepository.save(emptyTable);

        assertThatThrownBy(() -> orderService.create(requestWithEmptyTable))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("정상적으로 저장된 Order를 모두 조회한다.")
    @Test
    void list() {
        Order order1 = OrderFixture.createWithId(1L, OrderFixture.MEAL_STATUS, 1L);
        Order order2 = OrderFixture.createWithId(2L, OrderFixture.MEAL_STATUS, 1L);
        orderRepository.saveAll(Arrays.asList(order1, order2));

        assertThat(orderService.list())
            .usingRecursiveComparison()
            .isEqualTo(OrderResponse.listOf(Arrays.asList(order1, order2)));
    }

    @DisplayName("정상적으로 저장된 Order의 Status를 수정한다.")
    @Test
    void changeOrderStatus() {
        Order mealOrder = OrderFixture.createWithId(1L, OrderFixture.MEAL_STATUS, 1L);
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.COOKING);

        orderRepository.save(mealOrder);

        assertThat(orderService.changeOrderStatus(mealOrder.getId(), request))
            .extracting(OrderResponse::getOrderStatus)
            .isEqualTo(request.getOrderStatus());
    }

    @DisplayName("이미 완료된 Order의 Status를 수정 요청 시 예외를 반환한다")
    @Test
    void changeCompletionOrderStatus() {
        Order completeOrder = OrderFixture.createWithId(1L, OrderFixture.COMPLETION, 1L);
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.COOKING);

        orderRepository.save(completeOrder);

        assertThatThrownBy(
            () -> orderService.changeOrderStatus(completeOrder.getId(), request))
            .isInstanceOf(AlreadyCompleteOrderException.class);
    }
}
