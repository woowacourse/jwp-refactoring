package kitchenpos.application;

import kitchenpos.application.common.TestFixtureFactory;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/delete_all.sql")
class OrderServiceTest extends TestFixtureFactory {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("주문 생성 메서드 테스트")
    @Test
    void create() {
        OrderResponse orderResponse = orderService.create(makeOrderCreateRequest());

        List<OrderLineItem> orderLineItemsByOrderId = orderLineItemRepository.findAllByOrderId(orderResponse.getId());
        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getOrderLineItems()).hasSize(2),
                () -> assertThat(orderLineItemsByOrderId).hasSize(2),
                () -> assertThat(orderLineItemsByOrderId.get(0).getOrder().getId()).isEqualTo(orderResponse.getId())
        );
    }

    @DisplayName("주문 생성 메서드 - 테이블이 빈 테이블인 경우 예외 처리")
    @Test
    void createWhenEmptyTable() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, true);

        List<OrderLineItemDto> orderLineItemDtos = Arrays.asList(new OrderLineItemDto(1L, 1));
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), null, orderLineItemDtos);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 주문 요청 시 orderLineItems의 menuId가 존재하지 않는 menu의 아이디일 경우 예외처리")
    @Test
    void createWhenIllegalMenuId() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, true);
        OrderRequest orderRequest = new OrderRequest(savedOrderTable.getId(), null, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록 조회 메서드 테스트")
    @Test
    void list() {
        orderService.create(makeOrderCreateRequest());
        orderService.create(makeOrderCreateRequest());

        List<OrderResponse> list = orderService.list();
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list.get(0).getId()).isNotNull(),
                () -> assertThat(list.get(0).getOrderLineItems()).hasSize(2)
        );
    }

    @DisplayName("주문 상태 변경 메서드 테스트")
    @Test
    void changeOrderStatus() {
        OrderResponse savedOrder = orderService.create(makeOrderCreateRequest());
        OrderRequest orderRequest = new OrderRequest(null, OrderStatus.MEAL, null);

        OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), orderRequest);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태 변경 - 이미 완료 상태인 경우 예외 처리")
    @Test
    void changeOrderStatusWhenCompletion() {
        OrderResponse savedOrder = orderService.create(makeOrderCreateRequest());
        OrderRequest orderRequest = new OrderRequest(null, OrderStatus.COMPLETION, null);

        orderService.changeOrderStatus(savedOrder.getId(), orderRequest);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderLineItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        menuRepository.deleteAll();
        menuGroupRepository.deleteAll();
        productRepository.deleteAll();
    }
}
