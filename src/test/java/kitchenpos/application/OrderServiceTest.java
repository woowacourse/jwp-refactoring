package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private Long validOrderTableId;
    private Long validMenuId;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
        final OrderTable orderTable = orderTableDao.save(createOrderTable(1, false));
        validOrderTableId = orderTable.getId();
        final MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("추가메뉴"));
        final Menu menu = menuDao.save(createMenu("후라후라후라", 27_000L, menuGroup.getId()));
        validMenuId = menu.getId();
    }

    @DisplayName("주문을 저장한다.")
    @Test
    void create() {
        // given
        final OrderLineItem orderLineItemRequest = createOrderLineItemRequest(validMenuId, 2);
        final Order orderRequest = createOrderRequest(validOrderTableId, List.of(orderLineItemRequest));

        // when
        final Order savedOrder = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getId()).isNotNull()
        );
    }

    @DisplayName("주문 저장 시에 주문 항목이 없다면 예외를 반환한다.")
    @Test
    void create_throwException_ifOrderLineItemsEmpty() {
        // given
        final Order orderRequest = createOrderRequest(validOrderTableId, List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @DisplayName("없는 메뉴를 주문 시 예외를 반환한다.")
    @Test
    void create_throwException_ifMenuNotExist() {
        // given
        final Long noExistMenuId = 999L;
        final OrderLineItem orderLineItemRequest = createOrderLineItemRequest(noExistMenuId, 2);
        final Order order = createOrderRequest(validOrderTableId, List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 메뉴는 주문할 수 없습니다.");
    }

    @DisplayName("없는 테이블에서 주문 시 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotExist() {
        // given
        final Long noExistTableId = 999L;
        final OrderLineItem orderLineItemRequest = createOrderLineItemRequest(validMenuId, 2);
        final Order order = createOrderRequest(noExistTableId, List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("없는 테이블에서는 주문할 수 없습니다.");
    }

    @DisplayName("주문하려는 테이블이 비어있으면 예외를 반환한다.")
    @Test
    void create_throwException_ifTableNotEmpty() {
        // given
        final OrderTable emptyOrderTable = orderTableDao.save(createOrderTable(0, true));
        final OrderLineItem orderLineItemRequest = createOrderLineItemRequest(validMenuId, 2);
        final Order order = createOrderRequest(emptyOrderTable.getId(), List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 중이지 않은 테이블입니다.");
    }

    @DisplayName("전체 주문을 조회한다.")
    @Test
    void findAll() {
        // given
        final OrderLineItem orderLineItemRequest = createOrderLineItemRequest(validMenuId, 2);
        final Order order = createOrderRequest(validOrderTableId, List.of(orderLineItemRequest));
        orderService.create(order);

        // when, then
        assertThat(orderService.findAll()).hasSize(1);
    }

    private OrderLineItem createOrderLineItemRequest(final Long menuId, final long quantity) {

        final OrderLineItem orderLineItemRequest = new OrderLineItem();
        orderLineItemRequest.setMenuId(menuId);
        orderLineItemRequest.setQuantity(quantity);
        return orderLineItemRequest;
    }

    private Order createOrderRequest(final Long orderTableId, final List<OrderLineItem> orderLineItemRequests) {
        final Order orderRequest = new Order();
        orderRequest.setOrderTableId(orderTableId);
        orderRequest.setOrderLineItems(orderLineItemRequests);
        return orderRequest;
    }
}
