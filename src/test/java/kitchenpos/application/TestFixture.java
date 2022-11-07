package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    private TestFixture() {
    }

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static MenuProduct 메뉴_상품_생성(final Long productId, final Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct 메뉴_상품_생성(final Long menuId, final Long productId, final Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Menu 메뉴_생성(final String name, final BigDecimal price, final Long groupId,
                             final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(groupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu 메뉴_생성(final Long menuId, final String name, final BigDecimal price, final Long groupId,
                             final List<MenuProduct> menuProducts) {
        final Menu menu = new Menu();
        menu.setId(menuId);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(groupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static OrderTable 주문_테이블_생성() {
        return new OrderTable();
    }

    public static OrderTable 주문_테이블_생성(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 주문_테이블_생성(final long tableGroupId, final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 주문_테이블_생성(final long tableGroupId, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup 테이블_그룹_생성(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup 테이블_그룹_생성(final Long id, final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup 테이블_그룹과_주문_테이블_생성() {
        return new TableGroup(null, LocalDateTime.now(), List.of(new OrderTable(), new OrderTable()));
    }

    public static TableGroup 테이블_그룹_생성() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static OrderLineItem 주문_상품_생성(final Long menuId) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderMenuId(menuId);
        return orderLineItem;
    }

    public static Order 주문_생성(final List<OrderLineItem> orderLineItems, final Long orderTableId) {
        final Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        return order;
    }

    public static Order 주문_생성(final List<OrderLineItem> orderLineItems, final Long orderTableId,
                              final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderStatus(orderStatus);
        return order;
    }

    public static Order 주문_생성(final OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }
}
