package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {
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
    private MenuGroup menuGroup;
    private Product productA;
    private Product productB;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private MenuProductRequest menuProductA;
    private MenuProductRequest menuProductB;
    private Menu menu;
    private OrderTable tableA;

    @BeforeEach
    void setUpOrder() {
        productA = productService.create(new ProductCreateRequest("순살 까르보치킨", new BigDecimal(20000)));
        productB = productService.create(new ProductCreateRequest("순살 짜장치킨", new BigDecimal(18000)));

        menuGroup = menuGroupService.create(new MenuGroupCreateRequest("순살 두 마리"));

        name = "순살 까르보 한 마리 + 순살 짜장 한 마리";
        price = new BigDecimal(35000);
        menuGroupId = menuGroup.getId();
        menuProductA = new MenuProductRequest(productA.getId(), 1L);
        menuProductB = new MenuProductRequest(productB.getId(), 1L);
        menu = menuService.create(new MenuCreateRequest(name, price, menuGroupId, List.of(menuProductA, menuProductB)));

        tableA = tableService.create(new OrderTableCreateRequest(0, false));
    }

    @DisplayName("주문을 생성할 수 있다")
    @Test
    void create() {
        // given
        final var orderRequest = new OrderCreateRequest(
                tableA.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L))
        );

        // when
        final var beforeOrder = LocalDateTime.now();
        final var actual = orderService.create(orderRequest);
        final var orderId = actual.getId();
        final var afterOrder = LocalDateTime.now();

        // then
        assertAll(
                () -> assertThat(orderId).isEqualTo(1L),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(tableA.getId()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderedTime()).isAfter(beforeOrder),
                () -> assertThat(actual.getOrderedTime()).isBefore(afterOrder),
                () -> assertThat(actual.getOrderLineItems()).extracting("orderId").containsExactly(orderId),
                () -> assertThat(actual.getOrderLineItems()).extracting("menuId")
                        .containsExactly(menu.getId()),
                () -> assertThat(actual.getOrderLineItems()).extracting("quantity").containsExactly(1L)
        );
    }

    @DisplayName("create 메서드는")
    @Nested
    class Create {
        // final OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 1L);
        @DisplayName("주문 내역이 null이면 예외가 발생한다")
        @Test
        void should_fail_when_orderLineItems_is_null() {
            // given
            final var orderRequest = new OrderCreateRequest(tableA.getId(), null);

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 내역이 비어있으면 예외가 발생한다")
        @Test
        void should_fail_when_orderLineItems_is_empty() {
            // given
            final var orderRequest = new OrderCreateRequest(tableA.getId(), new ArrayList<>());

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 아이디를 전달하면 예외가 발생한다")
        @Test
        void should_fail_when_menuId_is_invalid() {
            // given
            final var orderRequest = new OrderCreateRequest(
                    tableA.getId(),
                    List.of(new OrderLineItemRequest(-1L, 1L))
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블 아이디로 null을 전달하면 예외가 발생한다")
        @Test
        void should_fail_when_orderTableId_is_null() {
            // given
            final var orderRequest = new OrderCreateRequest(
                    null,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블 아이디를 전달하면 예외가 발생한다")
        @Test
        void should_fail_when_orderTableId_is_invalid() {
            // given
            final var orderRequest = new OrderCreateRequest(
                    -1L,
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 대상 테이블이 주문 불가 상태(empty=true)면 예외가 발생한다")
        @Test
        void should_fail_when_orderTableId_is_empty() {
            // given
            tableService.changeEmpty(tableA.getId(), new OrderTableChangeEmptyRequest(true));
            final var orderRequest = new OrderCreateRequest(
                    tableA.getId(),
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("전체 주문을 조회할 수 있다")
    @Test
    void list() {
        // given
        final var orderRequest = new OrderCreateRequest(
                tableA.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L))
        );

        // when
        final var beforeOrder = LocalDateTime.now();
        orderService.create(orderRequest);
        final var afterOrder = LocalDateTime.now();

        // when
        final var actual = orderService.list();
        final var order = actual.iterator().next();

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(1),
                () -> assertThat(order.getId()).isEqualTo(1L),
                () -> assertThat(order.getOrderTableId()).isEqualTo(tableA.getId()),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderedTime()).isAfter(beforeOrder),
                () -> assertThat(order.getOrderedTime()).isBefore(afterOrder),
                () -> assertThat(order.getOrderLineItems()).extracting("menuId").containsExactly(menu.getId()),
                () -> assertThat(order.getOrderLineItems()).extracting("quantity").containsExactly(1L)
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus() {
        // given
        final var orderRequest = new OrderCreateRequest(
                tableA.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L))
        );
        final var order = orderService.create(orderRequest);

        // when
        final var expectedStatus = OrderStatus.COMPLETION.name();
        final var statusChangedOrder = orderService.changeOrderStatus(
                order.getId(),
                new OrderChangeStatusRequest(expectedStatus)
        );

        // then
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(statusChangedOrder.getOrderStatus()).isEqualTo(expectedStatus),
                () -> assertThat(order.getId()).isEqualTo(statusChangedOrder.getId())
        );
    }

    @DisplayName("changeOrderStatus 메서드는")
    @Nested
    class ChangeOrderStatus {
        @DisplayName("주문 아이디가 null일 경우 예외가 발생한다.")
        @Test
        void should_fail_when_orderId_is_null() {
            // given
            final var orderRequest = new OrderCreateRequest(
                    tableA.getId(),
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );
            orderService.create(orderRequest);

            // when
            final Long nullOrderId = null;
            final var request = new OrderChangeStatusRequest(OrderStatus.COMPLETION.name());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(nullOrderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 아이디일 경우 예외가 발생한다.")
        @Test
        void should_fail_when_orderId_is_invalid() {
            // given
            final var invalidOrderId = -1L;
            final var request = new OrderChangeStatusRequest(OrderStatus.COMPLETION.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 아이디로 조회한 주문이 이미 계산 완료라면(OrderStatus=COMPLETION) 예외가 발생한다.")
        @CsvSource(value = {"COOKING", "MEAL", "COMPLETION"})
        @ParameterizedTest
        void should_fail_when_order_is_complete(final OrderStatus orderStatus) {
            // given
            final var orderRequest = new OrderCreateRequest(
                    tableA.getId(),
                    List.of(new OrderLineItemRequest(menu.getId(), 1L))
            );
            final var order = orderService.create(orderRequest);
            orderService.changeOrderStatus(
                    order.getId(),
                    new OrderChangeStatusRequest(OrderStatus.COMPLETION.name())
            );

            // when
            final var orderId = order.getId();
            final var request = new OrderChangeStatusRequest(orderStatus.name());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
