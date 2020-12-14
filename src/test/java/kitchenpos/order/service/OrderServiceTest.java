package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.enums.OrderStatus;
import kitchenpos.order.dto.OrderChangeStatusRequest;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class OrderServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderService orderService;

    @DisplayName("create: 주문 등록 테스트")
    @Test
    void createTest() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuRepository.save(
                new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup, new ArrayList<>()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        final OrderResponse order = orderService.create(orderCreateRequest);

        assertAll(
                () -> assertThat(order).isNotNull(),
                () -> assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId())
        );
    }

    @DisplayName("create: 주문 등록 시 테이블이 비어있는 경우 예외처리")
    @Test
    void createTestByOrderTableEmpty() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuRepository.save(
                new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup, new ArrayList<>()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list: 전체 주문을 확인하는 테스트")
    @Test
    void listTest() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuRepository.save(
                new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup, new ArrayList<>()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        orderService.create(orderCreateRequest);

        final List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getId()).isNotNull()
        );
    }

    @DisplayName("changeOrderStatus: 주문 상태를 변경하는 테스트")
    @Test
    void changeOrderStatusTest() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuRepository.save(
                new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup, new ArrayList<>()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        final OrderResponse order = orderService.create(orderCreateRequest);

        final OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest("MEAL");

        final OrderResponse orderResponse = orderService.changeOrderStatus(order.getId(), orderChangeStatusRequest);

        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("changeOrderStatus: 주문 상태가 Completion일 경우 예외처리")
    @Test
    void changeOrderStatusTestByCompletionOfOrderState() {
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuRepository.save(
                new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup, new ArrayList<>()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        final OrderResponse order = orderService.create(orderCreateRequest);

        orderService.changeOrderStatus(order.getId(), new OrderChangeStatusRequest("COMPLETION"));
        final OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderChangeStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
