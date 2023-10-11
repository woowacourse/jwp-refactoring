package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 서비스 테스트")
@ServiceTest
class OrderServiceTest {

    private final OrderService orderService;
    private final TableService tableService;
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public OrderServiceTest(final OrderService orderService, final TableService tableService,
                            final MenuService menuService,
                            final MenuGroupService menuGroupService, final ProductService productService) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    private Menu setUpMenu() {
        // given
        final Product product = productService.create(ProductFixture.create());
        final MenuGroup menuGroup = menuGroupService.create(MenuGroupFixture.create());
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(product.getId());

        final Menu menu = new Menu();
        menu.setPrice(product.getPrice().multiply(BigDecimal.valueOf(2)).subtract(BigDecimal.ONE));
        menu.setName("상품+상품");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        return menuService.create(menu);
    }

    @Nested
    @DisplayName("주문을 생성할 때")
    class Create {

        @DisplayName("조리 상태로 생성된다")
        @Test
        void success() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(setUpMenu().getId());
            orderLineItem.setQuantity(2);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));
            final Order savedOrder = orderService.create(order);

            assertSoftly(softly -> {
                assertThat(savedOrder.getId()).isNotNull();
                assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
            });
        }

        @DisplayName("주문할 메뉴와 수량 목록이 비어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderLineItemEmpty() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴를 주문하면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidMenu() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(-1L);
            orderLineItem.setQuantity(2);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블로 주문하면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrderTable() {
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(setUpMenu().getId());
            orderLineItem.setQuantity(2);

            final Order order = new Order();
            order.setOrderTableId(-1L);
            order.setOrderLineItems(List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 빈 테이블이면 예외처리 한다")
        @Test
        void throwExceptionWhenEmptyTable() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createEmpty());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(setUpMenu().getId());
            orderLineItem.setQuantity(2);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void findAllOrders() {
        final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(setUpMenu().getId());
        orderLineItem.setQuantity(2);

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        orderService.create(order);

        final List<Order> list = orderService.list();

        assertThat(list).hasSize(1);
    }

    @Nested
    @DisplayName("주문 상태를 변경할 때")
    class ChangeOrderStatus {

        @DisplayName("주문 상태는 조리 ➜ 식사 ➜ 계산완료 순서로 변경된다")
        @Test
        void success() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(setUpMenu().getId());
            orderLineItem.setQuantity(2);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));
            final Order savedOrder = orderService.create(order);
            assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");

            final Order change1 = new Order();
            change1.setOrderStatus("MEAL");
            final Order savedOrder2 = orderService.changeOrderStatus(savedOrder.getId(), change1);
            assertThat(savedOrder2.getOrderStatus()).isEqualTo("MEAL");

            final Order change2 = new Order();
            change2.setOrderStatus("COMPLETION");
            final Order savedOrder3 = orderService.changeOrderStatus(savedOrder2.getId(), change2);
            assertThat(savedOrder3.getOrderStatus()).isEqualTo("COMPLETION");
        }

        @DisplayName("존재하지 않는 주문이면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrder() {
            final Order order = new Order();
            order.setOrderStatus("MEAL");

            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산 완료 상태를 변경 하려하면 예외처리 한다")
        @Test
        void throwExceptionWhenChangeCompletionStatus() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(setUpMenu().getId());
            orderLineItem.setQuantity(2);

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem));
            final Order savedOrder = orderService.create(order);

            final Order change = new Order();
            change.setOrderStatus("COMPLETION");
            orderService.changeOrderStatus(savedOrder.getId(), change);

            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), change))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
