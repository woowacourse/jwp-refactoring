package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.exception.badrequest.OrderAlreadyCompletedException;
import kitchenpos.exception.badrequest.OrderFailureOnEmptyOrderTableException;
import kitchenpos.exception.badrequest.OrderIdInvalidException;
import kitchenpos.exception.badrequest.OrderTableIdInvalidException;
import kitchenpos.exception.notfound.OrderNotFoundException;
import kitchenpos.exception.notfound.OrderTableNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderingMenu;
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
import org.springframework.jdbc.core.JdbcTemplate;

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
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MenuRepository menuRepository;
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
        productA = productService.create(new ProductCreateRequest("순살 까르보치킨", new BigDecimal("20000.00")));
        productB = productService.create(new ProductCreateRequest("순살 짜장치킨", new BigDecimal("18000.00")));

        menuGroup = menuGroupService.create(new MenuGroupCreateRequest("순살 두 마리"));

        name = "순살 까르보 한 마리 + 순살 짜장 한 마리";
        price = new BigDecimal("35000.00");
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
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(actual.getOrderedTime()).isAfter(beforeOrder),
                () -> assertThat(actual.getOrderedTime()).isBefore(afterOrder),
                () -> assertThat(actual.getOrderLineItems().getOrderLineItems())
                        .extracting("order")
                        .extracting("id")
                        .containsExactly(orderId),
                () -> assertThat(actual.getOrderLineItems()
                        .getOrderLineItems())
                        .extracting("menuId")
                        .containsExactly(menu.getId()),
                () -> assertThat(actual.getOrderLineItems().getOrderLineItems())
                        .extracting("quantity")
                        .containsExactly(1L)
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
                    .isInstanceOf(OrderTableIdInvalidException.class);
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
                    .isInstanceOf(OrderTableNotFoundException.class);
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
                    .isInstanceOf(OrderFailureOnEmptyOrderTableException.class);
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
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(order.getOrderedTime()).isAfter(beforeOrder),
                () -> assertThat(order.getOrderedTime()).isBefore(afterOrder),
                () -> assertThat(order.getOrderLineItems().getOrderLineItems())
                        .extracting("menuId")
                        .containsExactly(menu.getId()),
                () -> assertThat(order.getOrderLineItems().getOrderLineItems())
                        .extracting("quantity")
                        .containsExactly(1L)
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
        final var expectedStatus = OrderStatus.COMPLETION;
        final var statusChangedOrder = orderService.changeOrderStatus(
                order.getId(),
                new OrderChangeStatusRequest(expectedStatus.name())
        );

        // then
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
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
                    .isInstanceOf(OrderIdInvalidException.class);
        }

        @DisplayName("존재하지 않는 주문 아이디일 경우 예외가 발생한다.")
        @Test
        void should_fail_when_orderId_is_invalid() {
            // given
            final var invalidOrderId = -1L;
            final var request = new OrderChangeStatusRequest(OrderStatus.COMPLETION.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, request))
                    .isInstanceOf(OrderNotFoundException.class);
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
                    .isInstanceOf(OrderAlreadyCompletedException.class);
        }
    }

    @DisplayName("주문 생성 후 메뉴 이름을 수정해도 주문 정보는 수정되지 않는다")
    @Test
    void testMethodNameHere() {
        // given
        final var order = createOrder();
        final var menuNamesBefore = findOrderedMenuNames(order);
        final var orderedMenuIds = menuIds(order);
        final var newName = "메뉴명 수정";

        // when
        updateMenuNamesByIdIn(orderedMenuIds, newName);
        final var updatedMenuNames = findUpdatedMenuNames(orderedMenuIds);
        final var menuNamesAfter = findOrderedMenuNames(order);

        // then
        assertAll(
                () -> assertThat(menuNamesBefore).containsExactly(name),
                () -> assertThat(updatedMenuNames).containsExactly(newName),
                () -> assertThat(menuNamesAfter).containsExactly(name)
        );
    }

    private Order createOrder() {
        final var orderRequest = new OrderCreateRequest(
                tableA.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L))
        );

        return orderService.create(orderRequest);
    }

    private List<String> findOrderedMenuNames(final Order order) {
        return orderService.list()
                .stream()
                .filter(savedOrder -> Objects.equals(savedOrder.getId(), order.getId()))
                .findAny()
                .orElseThrow()
                .getOrderLineItems()
                .getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuName)
                .collect(Collectors.toList());
    }

    private List<Long> menuIds(final Order order) {
        return order.getOrderLineItems()
                .getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private void updateMenuNamesByIdIn(final List<Long> orderedMenuIds, final String newMenuName) {
        final var menuIdsJoined = orderedMenuIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        new JdbcTemplate(dataSource)
                .execute(String.format("update menu set name = '%s' where id in (%s)", newMenuName, menuIdsJoined));
    }

    private List<String> findUpdatedMenuNames(final List<Long> orderedMenuIds) {
        return menuRepository.findByIdIn(orderedMenuIds)
                .stream()
                .map(OrderingMenu::getMenuName)
                .collect(Collectors.toList());
    }
}
