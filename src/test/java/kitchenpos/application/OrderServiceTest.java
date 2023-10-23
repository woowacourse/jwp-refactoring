package kitchenpos.application;

import static kitchenpos.Fixture.INVALID_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.OrderChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrationTest {
    private static final Order ORDER_STATUS_COOKING = new Order(OrderStatus.COOKING);

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuResponse menu;

    @BeforeEach
    void setUp() {
        final MenuGroupResponse menuGroup = menuGroupService.create(Fixture.MENU_GROUP);
        final ProductResponse product = productService.create(Fixture.PRODUCT);
        final MenuCreateRequest menu = new MenuCreateRequest("후라이드+후라이드",
                BigDecimal.valueOf(19000),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 2)));
        this.menu = menuService.create(menu);
    }

    @Test
    void create() {
        // given
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1)));

        // when
        final OrderResponse result = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isNotNull();
            softly.assertThat(result.getOrderedTime()).isNotNull();
            softly.assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        });
    }

    @Test
    void create_duplicatedMenuException() {
        // given
        final OrderLineItemRequest orderLineItem1 = new OrderLineItemRequest(menu.getId(), 1);
        final OrderLineItemRequest orderLineItem2 = new OrderLineItemRequest(menu.getId(), 2);
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(),
                List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableNullException() {
        // given
        final OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menu.getId(), 1);
        final OrderCreateRequest order = new OrderCreateRequest(INVALID_ID, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableEmptyException() {
        // given
        final OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menu.getId(), 1);
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menu.getId(), 1);
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);

        final OrderResponse order1 = orderService.create(
                new OrderCreateRequest(orderTable1.getId(), List.of(orderLineItem)));
        final OrderResponse order2 = orderService.create(
                new OrderCreateRequest(orderTable2.getId(), List.of(orderLineItem)));

        // when
        final List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(order1, order2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(final String status) {
        // given
        final OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menu.getId(), 1);
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderResponse saved = orderService.create(
                new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem)));

        // when
        final OrderChangeOrderStatusRequest changeRequest = new OrderChangeOrderStatusRequest(status);
        final OrderResponse result = orderService.changeOrderStatus(saved.getId(), changeRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderStatus()).isEqualTo(status);
            softly.assertThat(result).usingRecursiveComparison()
                    .ignoringFields("orderStatus")
                    .isEqualTo(saved);
        });
    }

    @Test
    void changeOrderStatus_orderNullException() {
        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(INVALID_ID, new OrderChangeOrderStatusRequest("COOKING")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus_orderCompletedException() {
        // given
        final OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menu.getId(), 1);
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderResponse saved = orderService.create(
                new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem)));
        orderService.changeOrderStatus(saved.getId(), new OrderChangeOrderStatusRequest("COMPLETION"));

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(saved.getId(), new OrderChangeOrderStatusRequest("COOKING")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
