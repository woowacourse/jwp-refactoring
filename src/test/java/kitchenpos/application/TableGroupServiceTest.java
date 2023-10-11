package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import({ProductService.class, JdbcTemplateProductDao.class, MenuService.class,
        JdbcTemplateMenuDao.class, MenuGroupService.class, JdbcTemplateMenuGroupDao.class,
        JdbcTemplateMenuProductDao.class, JdbcTemplateOrderTableDao.class, OrderService.class,
        JdbcTemplateOrderDao.class, TableService.class,
        JdbcTemplateOrderLineItemDao.class})
class TableGroupServiceTest {

    @Autowired
    private DataSource dataSource;
    private TableGroupService tableGroupService;

    @BeforeEach
    void SetUp() {
        this.tableGroupService = new TableGroupService(
                new JdbcTemplateOrderDao(dataSource),
                new JdbcTemplateOrderTableDao(dataSource),
                new JdbcTemplateTableGroupDao(dataSource)
        );
    }

    @Nested
    @DisplayName("주문 테이블을 그룹화할 때")
    class GroupOrderTables {
        @Test
        @DisplayName("그룹화할 주문 테이블들의 식별자를 제공하여 그룹화할 수 있다.")
        void given(@Autowired TableService tableService) {
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(true);
            final OrderTable savedOrderTable2 = tableService.create(orderTable2);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable, savedOrderTable2));
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertThat(savedTableGroup).isNotNull();
            assertThat(savedTableGroup.getId()).isNotNull();
            assertThat(savedTableGroup.getCreatedDate())
                    .as("식별자와 생성일자는 자동 생성된다")
                    .isNotNull();
        }

        @Test
        @DisplayName("그룹화하려는 주문 테이블의 수가 2개 미만이면 그룹화할 수 없다.")
        void invalidOrderTableSize(@Autowired TableService tableService) {
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화하려는 주문 테이블 중 실재하지 않는 주문 테이블이 있다면 그룹화할 수 없다.")
        void notExistingOrderTable(@Autowired TableService tableService) {
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(true); // 저장하지 않은 orderTable

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹화하려는 주문 테이블 중 이미 그룹화 되어있는 주문 테이블이 있다면 그룹화할 수 없다.")
        void alreadyGrouped(@Autowired TableService tableService) {
            // given : 이미 그룹화된 주문 테이블
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(true);
            final OrderTable savedOrderTable2 = tableService.create(orderTable2);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable, savedOrderTable2));
            tableGroupService.create(tableGroup);

            final OrderTable orderTable3 = new OrderTable();
            orderTable3.setEmpty(true);
            final OrderTable savedOrderTable3 = tableService.create(orderTable3);

            final TableGroup tableGroup2 = new TableGroup();
            tableGroup2.setOrderTables(List.of(savedOrderTable2, savedOrderTable3));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 테이블의 그룹화를 해제할 때")
    class UngroupOrderTables {
        @Test
        @DisplayName("tableGroupId에 해당하는 pathVariable에 단체 식별자를 제공하여 그룹화를 해제할 수 있다.")
        void given(@Autowired TableService tableService) {
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(true);
            final OrderTable savedOrderTable2 = tableService.create(orderTable2);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable, savedOrderTable2));
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            assertThat(
                    savedTableGroup.getOrderTables()
                            .stream().map(OrderTable::getId)
                            .collect(Collectors.toList())
            ).contains(savedOrderTable.getId(), savedOrderTable2.getId());
        }

        @Test
        @DisplayName("그룹에 속한 주문 테이블 중 주문의 상태가 COOKING 또는 MEAL인 경우 그룹화를 해제할 수 없다.")
        void invalidOrderStatus(
                @Autowired ProductService productService,
                @Autowired MenuService menuService,
                @Autowired MenuGroupService menuGroupService,
                @Autowired TableService tableService,
                @Autowired OrderService orderService
        ) {

            // given : 주문 테이블
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            final OrderTable orderTable2 = new OrderTable();
            orderTable2.setEmpty(true);
            final OrderTable savedOrderTable2 = tableService.create(orderTable2);

            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(savedOrderTable, savedOrderTable2));
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            주어진_OrderTable로_주문하기(productService, menuService, menuGroupService, orderService, savedOrderTable);

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Order 주어진_OrderTable로_주문하기(
                final ProductService productService,
                final MenuService menuService,
                final MenuGroupService menuGroupService,
                final OrderService orderService,
                final OrderTable orderTable) {

            // given : 상품
            final Product product = new Product();
            product.setName("상품!");
            product.setPrice(new BigDecimal("4000"));
            final Product savedProduct = productService.create(product);

            // given : 메뉴 그룹
            final MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("메뉴 그룹!");
            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            // given : 메뉴
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(4L);

            final Menu menu = new Menu();
            menu.setName("메뉴!");
            menu.setPrice(new BigDecimal("4000"));
            menu.setMenuProducts(List.of(menuProduct));
            menu.setMenuGroupId(savedMenuGroup.getId());
            final Menu savedMenu = menuService.create(menu);

            final Menu menu2 = new Menu();
            menu2.setName("메뉴 2!");
            menu2.setPrice(new BigDecimal("9000"));
            menu2.setMenuProducts(List.of(menuProduct));
            menu2.setMenuGroupId(savedMenuGroup.getId());
            final Menu savedMenu2 = menuService.create(menu2);

            // given : 주문 메뉴
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setQuantity(4);
            orderLineItem.setMenuId(savedMenu.getId());

            final OrderLineItem orderLineItem2 = new OrderLineItem();
            orderLineItem2.setQuantity(3);
            orderLineItem2.setMenuId(savedMenu2.getId());

            // given : 주문
            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());
            order.setOrderLineItems(List.of(orderLineItem, orderLineItem2));
            return orderService.create(order);
        }
    }
}
