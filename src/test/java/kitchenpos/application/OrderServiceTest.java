package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.menu.MenuRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.application.dto.menu.ProductQuantityDto;
import kitchenpos.application.dto.order.MenuQuantityDto;
import kitchenpos.application.dto.order.OrderRequest;
import kitchenpos.application.dto.order.OrderResponse;
import kitchenpos.application.dto.order.OrderStatusChangeRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends DataDependentIntegrationTest {

    private Long menuId;
    private MenuGroup menuGroup;
    private OrderTable orderTable;
    private static final long NOT_EXIST_ID = Long.MAX_VALUE;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("product", BigDecimal.valueOf(1000L)));
        final MenuResponse menu = menuService.create(new MenuRequest("menu", BigDecimal.valueOf(1000L), menuGroup.getId(),
            List.of(new ProductQuantityDto(product.getId(), 2)
            )));
        menuId = menu.getId();
        orderTable = orderTableRepository.save(new OrderTable(3, false));
    }

    @DisplayName("새 주문을 저장한다.")
    @Test
    void create_success() {
        // given
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(menuId, 3)));

        // when
        final OrderResponse savedOrder = orderService.create(orderRequest);

        // then
        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId()),
            () -> assertThat(savedOrder.getOrderLineItems()).usingRecursiveComparison()
                .isEqualTo(List.of(new MenuQuantityDto(menuId, 3)))
        );
    }

    @DisplayName("주문 저장 시, 주문할 메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void create_empty_fail() {
        // given
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 존재하지 않는 메뉴가 있다면 예외가 발생한다.")
    @Test
    void create_notExistMenu_fail() {
        // given
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(NOT_EXIST_ID, 3)));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 주문 테이블이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_notExistTable_fail() {
        // given
        final OrderRequest orderRequest = new OrderRequest(NOT_EXIST_ID, List.of(new MenuQuantityDto(menuId, 3)));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void create_emptyTable_fail() {
        // given
        orderTable.updateEmpty(true);
        orderTableRepository.save(orderTable);

        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(menuId, 3)));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 Order 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(menuId, 3)));
        final OrderResponse createdOrder = orderService.create(orderRequest);
        final OrderRequest orderRequest2 = new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(menuId, 2)));
        final OrderResponse createdOrder2 = orderService.create(orderRequest2);

        // when
        final List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison()
            .ignoringFields("orderedTime")
            .isEqualTo(List.of(createdOrder, createdOrder2));
    }

    @DisplayName("Order 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        // given
        final OrderResponse prevOrder = orderService.create(new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(menuId, 3))));
        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        // when
        final OrderResponse changedOrder = orderService.changeOrderStatus(prevOrder.getId(), statusChangeRequest);

        // then
        assertThat(changedOrder.getId()).isEqualTo(prevOrder.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("Order 상태를 바꿀 때, 존재하지 않는 주문이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_notExist_fail() {
        // given
        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(NOT_EXIST_ID, statusChangeRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 상태를 바꿀 때, 상태가 completion 이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_wrongStatus_fail() {
        // given
        final OrderResponse prevOrder = orderService.create(new OrderRequest(orderTable.getId(), List.of(new MenuQuantityDto(menuId, 3))));
        final OrderResponse completedOrder = orderService.changeOrderStatus(prevOrder.getId(), new OrderStatusChangeRequest(OrderStatus.COMPLETION.name()));
        final Long orderId = completedOrder.getId();

        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, statusChangeRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
