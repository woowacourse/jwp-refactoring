//package kitchenpos.support;
//
//import static kitchenpos.fixtures.MenuFixture.MENU;
//import static kitchenpos.fixtures.MenuGroupFixture.MENU_GROUP;
//import static kitchenpos.fixtures.MenuProductFixture.MENU_PRODUCT;
//import static kitchenpos.fixtures.OrderFixture.ORDER;
//import static kitchenpos.fixtures.OrderLineItemFixture.ORDER_LINE_ITEM;
//import static kitchenpos.fixtures.OrderTableFixture.ORDER_TABLE;
//import static kitchenpos.fixtures.ProductFixture.PRODUCT;
//import static kitchenpos.fixtures.TableGroupFixture.TABLE_GROUP;
//
//import java.util.List;
//import kitchenpos.application.MenuGroupService;
//import kitchenpos.application.MenuService;
//import kitchenpos.application.OrderService;
//import kitchenpos.application.ProductService;
//import kitchenpos.application.TableGroupService;
//import kitchenpos.application.TableService;
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.MenuGroup;
//import kitchenpos.domain.MenuProduct;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.Product;
//import kitchenpos.domain.TableGroup;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestSupporter {
//
//    @Autowired
//    private MenuGroupService menuGroupService;
//
//    @Autowired
//    private MenuService menuService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private TableGroupService tableGroupService;
//
//    @Autowired
//    private TableService tableService;
//
//    public Product createProduct() {
//        final Product product = PRODUCT();
//        return productService.create(product);
//    }
//
//    public MenuGroup createMenuGroup() {
//        final MenuGroup menuGroup = MENU_GROUP();
//        return menuGroupService.create(menuGroup);
//    }
//
//    public Menu createMenu() {
//        final Product product = createProduct();
//        final MenuGroup menuGroup = createMenuGroup();
//        final MenuProduct menuProduct = MENU_PRODUCT(product, 10L);
//        final Menu menu = MENU(menuGroup, List.of(menuProduct));
//        return menuService.create(menu);
//    }
//
//    public Order createOrder() {
//        final Menu menu = createMenu();
//        final OrderLineItem orderLineItem = ORDER_LINE_ITEM(menu, 10L);
//        final OrderTable orderTable = createOrderTable(false);
//        final Order order = ORDER(List.of(orderLineItem), orderTable);
//        return orderService.create(order);
//    }
//
//    public Order createOrder(final OrderTable orderTable) {
//        final Menu menu = createMenu();
//        final OrderLineItem orderLineItem = ORDER_LINE_ITEM(menu, 10L);
//        final Order order = ORDER(List.of(orderLineItem), orderTable);
//        return orderService.create(order);
//    }
//
//    public OrderTable createOrderTable(final boolean empty) {
//        final OrderTable orderTable = ORDER_TABLE();
//        orderTable.setEmpty(empty);
//        return tableService.create(orderTable);
//    }
//
//    public TableGroup createTableGroup(final List<OrderTable> orderTables) {
//        final TableGroup tableGroup = TABLE_GROUP(orderTables);
//        return tableGroupService.create(tableGroup);
//    }
//}
