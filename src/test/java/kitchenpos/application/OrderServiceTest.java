package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("주문 서비스 테스트")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;


    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Menu menu = menuRepository.save(new Menu("menu", BigDecimal.valueOf(1000), menuGroup));
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menu.getId(), 2L);

        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
            Collections.singletonList(orderLineItemCreateRequest));
        OrderResponse response = orderService.create(request);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(response.getOrderTableId()).isEqualTo(orderTable.getId())
        );
    }

    @DisplayName("주문 리스트를 불러온다.")
    @Test
    void list() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Menu menu = menuRepository.save(new Menu("menu", BigDecimal.valueOf(1000), menuGroup));
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menu.getId(), 2L);

        OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(),
            Collections.singletonList(orderLineItemCreateRequest));
        orderService.create(request);
        orderService.create(request);

        List<OrderResponse> orders = orderService.list();
        assertThat(orders.size()).isEqualTo(2);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource({"MEAL, MEAL", "COMPLETION, COMPLETION"})
    void changeOrderStatus(OrderStatus input, OrderStatus expected) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        Menu menu = menuRepository.save(new Menu("menu", BigDecimal.valueOf(1000), menuGroup));
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menu.getId(), 2L);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
            Collections.singletonList(orderLineItemCreateRequest));
        OrderResponse savedOrder = orderService.create(orderCreateRequest);

        OrderStatusChangeRequest request = new OrderStatusChangeRequest(input);
        OrderResponse response = orderService.changeOrderStatus(savedOrder.getId(), request);

        assertThat(response.getOrderStatus()).isEqualTo(expected.name());
    }
}