package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class OrderServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    private Menu menu;
    private OrderTable table;

    @BeforeEach
    void setUp() {
        menu = createMenu_후라이드세트();
        table = createTable();
    }

    @Test
    void create() {
        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // when
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(table.getId());

        Order result = orderService.create(order);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        assertThat(result.getOrderTableId()).isEqualTo(table.getId());
    }

    @Test
    @DisplayName("create - 그룹에 속한 테이블에서 주문")
    void createWithGroupedTable() {
        // given
        OrderTable anotherTable = createTable();

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(anotherTable, table));
        tableGroupService.create(tableGroup);

        // when
        Order resultOfTable = orderWithEqualAmountOfAllMenus(table,
            Collections.singletonList(menu), 2);
        Order resultOfAnotherTable = orderWithEqualAmountOfAllMenus(anotherTable,
                Collections.singletonList(menu), 2);

        // then
        assertThat(resultOfTable.getId()).isNotNull();
        assertThat(resultOfTable.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        assertThat(resultOfTable.getOrderTableId()).isEqualTo(table.getId());

        assertThat(resultOfAnotherTable.getId()).isNotNull();
        assertThat(resultOfAnotherTable.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        assertThat(resultOfAnotherTable.getOrderTableId()).isEqualTo(anotherTable.getId());
    }

    @Test
    @DisplayName("create - 아무 메뉴도 포함하지 않는 주문시 예외처리")
    void create_IfOrderContainsNoMenu_ThrowException() {
        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // when
        Order order = new Order();
        order.setOrderLineItems(new ArrayList<>());
        order.setOrderTableId(table.getId());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 존재하지 않는 메뉴 id로 주문시 예외처리")
    void create_IfOrderContainsWrongMenu_ThrowException() {
        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // when
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1_000L);
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(table.getId());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 빈 테이블에서 주문 시도시 예외처리")
    void create_IfTableIsEmpty_ThrowException() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(table.getId());

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        assertThat(orderService.list()).hasSize(0);

        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // given (create order)
        orderWithEqualAmountOfAllMenus(table, Collections.singletonList(menu), 2);

        // when & then
        assertThat(orderService.list()).hasSize(1);
    }

    @Test
    @DisplayName("changeOrderStatus")
    void changeOrderStatus() {
        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // given (create order)
        Order order = orderWithEqualAmountOfAllMenus(table, Collections.singletonList(menu), 2);

        // when & then
        order.setOrderStatus(OrderStatus.MEAL.toString());
        assertThat(orderService.changeOrderStatus(order.getId(), order).getOrderStatus())
            .isEqualTo(OrderStatus.MEAL.toString());

        order.setOrderStatus(OrderStatus.COMPLETION.toString());
        assertThat(orderService.changeOrderStatus(order.getId(), order).getOrderStatus())
            .isEqualTo(OrderStatus.COMPLETION.toString());
    }

    @Test
    @DisplayName("changeOrderStatus - 요리중인 상태에서 식사를 거치지 않고 식사완료 상태로 바꾸려는 경우")
    void changeOrderStatus_IfAfterCookingIsCompletion() {
        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // given (create order)
        Order order = orderWithEqualAmountOfAllMenus(table, Collections.singletonList(menu), 2);

        // when & then
        order.setOrderStatus(OrderStatus.COMPLETION.toString());
        order = orderService.changeOrderStatus(order.getId(), order);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.toString());
    }

    @Test
    @DisplayName("changeOrderStatus - 존재하지 않는 상태 문자열 사용시 예외처리")
    void changeOrderStatus_IfTryWithUndefinedStatus_ThrowException() {
        // given (change table to not empty)
        table.setEmpty(false);
        table = tableService.changeEmpty(table.getId(), table);

        table.setNumberOfGuests(4);
        table = tableService.changeNumberOfGuests(table.getId(), table);

        // given (create order)
        Order order = orderWithEqualAmountOfAllMenus(table, Collections.singletonList(menu), 2);

        // when & then
        order.setOrderStatus("식사중");
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createTable() {
        // create table
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        return tableService.create(orderTable);
    }

    private Menu createMenu_후라이드세트() {
        // create products
        Product 후라이드치킨 = new Product();
        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(new BigDecimal(10_000));
        후라이드치킨 = productService.create(후라이드치킨);

        Product 프랜치프라이 = new Product();
        프랜치프라이.setName("프랜치프라이");
        프랜치프라이.setPrice(new BigDecimal(5_000));
        프랜치프라이 = productService.create(프랜치프라이);

        // create a menu group
        MenuGroup 세트메뉴 = new MenuGroup();
        세트메뉴.setName("세트메뉴");
        세트메뉴 = menuGroupService.create(세트메뉴);

        // create menu
        Menu menu = new Menu();
        menu.setName("후라이드 세트");
        menu.setPrice(new BigDecimal(13_000));
        menu.setMenuGroupId(세트메뉴.getId());
        menu.setMenuProducts(createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이)));

        return menuService.create(menu);
    }

    private List<MenuProduct> createMenuProductsWithAllQuantityAsOne(List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (Product product : products) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(1);

            menuProducts.add(menuProduct);
        }
        return Collections.unmodifiableList(menuProducts);
    }

    private Order orderWithEqualAmountOfAllMenus(OrderTable table, List<Menu> menus, int quantity) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (Menu menu : menus) {
            OrderLineItem orderLineItem = new OrderLineItem();

            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(2);

            orderLineItems.add(orderLineItem);
        }
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(table.getId());

        return orderService.create(order);
    }
}
