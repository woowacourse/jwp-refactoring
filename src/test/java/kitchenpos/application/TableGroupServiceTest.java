package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("단체 지정 서비스 테스트")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private OrderService orderService;

    @Nested
    @DisplayName("단체 지정을 할 때")
    class Create {

        @DisplayName("정상적으로 단체 지정된다")
        @Test
        void success() {
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @DisplayName("주문 테이블 목록이 비어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderTableEmpty() {
            final TableGroup tableGroup = new TableGroup();

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블 목록의 사이즈가 2 미만이면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderTableListSizeIsLowerThanTwo() {
            final OrderTable orderTable = tableService.create(OrderTableFixture.createEmpty());

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블이 들어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsInvalidOrderTable() {
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(-1L);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있지 않은 테이블이 포함되어 있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsNotEmptyOrderTable() {
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            orderTable1.setEmpty(false);
            tableService.changeEmpty(orderTable1.getId(), orderTable1);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체 지정이 된 테이블이 포함되어 있으면 예외처리 한다")
        @Test
        void throwExceptionWhenContainsAlreadyTableGroupingOrderTable() {
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup1 = new TableGroup();
            tableGroup1.setOrderTables(List.of(orderTable1, orderTable2));
            tableGroupService.create(tableGroup1);

            final OrderTable orderTable3 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup2 = new TableGroup();
            tableGroup2.setOrderTables(List.of(orderTable1, orderTable3));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("단체 지정을 해제할 때")
    class Ungroup {

        @DisplayName("정상적으로 해제할 수 있다")
        @Test
        void success() {
            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup1 = new TableGroup();
            tableGroup1.setOrderTables(List.of(orderTable1, orderTable2));

            assertThatCode(() -> tableGroupService.ungroup(tableGroup1.getId()))
                    .doesNotThrowAnyException();
        }

        @DisplayName("주문 상태가 계산 완료가 아닌 테이블이 포함되어 있다면 예외처리 한다")
        @ParameterizedTest
        @ValueSource(strings = {"MEAL", "COOKING"})
        void throwExceptionWhenContainsAlreadyTableGroupingOrderTable(String orderStatus) {
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
            final Menu savedMenu = menuService.create(menu);

            final OrderTable orderTable1 = tableService.create(OrderTableFixture.createEmpty());
            final OrderTable orderTable2 = tableService.create(OrderTableFixture.createEmpty());
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            tableGroupService.create(tableGroup);

            final OrderLineItem orderLineItem1 = new OrderLineItem();
            orderLineItem1.setMenuId(savedMenu.getId());
            orderLineItem1.setQuantity(1L);
            final Order order = new Order();
            order.setOrderTableId(orderTable1.getId());
            order.setOrderLineItems(List.of(orderLineItem1));
            final Order savedOrder = orderService.create(order);
            final Order change1 = new Order();
            change1.setOrderStatus(orderStatus);

            orderService.changeOrderStatus(savedOrder.getId(), change1);

            final OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setMenuId(savedMenu.getId());
            orderLineItem2.setQuantity(1L);
            final Order order2 = new Order();
            order2.setOrderTableId(orderTable2.getId());
            order2.setOrderLineItems(List.of(orderLineItem2));
            final Order savedOrder2 = orderService.create(order);
            final Order change2 = new Order();
            change2.setOrderStatus("COMPLETION");

            orderService.changeOrderStatus(savedOrder2.getId(), change2);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
