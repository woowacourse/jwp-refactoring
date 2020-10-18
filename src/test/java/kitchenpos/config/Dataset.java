package kitchenpos.config;

import kitchenpos.domain.*;
import org.assertj.core.util.Lists;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Dataset {

    public static MenuGroup menuGroup_패스트_푸드() {
        MenuGroup menuGroup = new MenuGroup(1L, "패스트 푸드");
        return menuGroup;
    }

    public static Product product_포테이토_피자() {
        Product product = new Product(5L, "포테이토 피자", BigDecimal.valueOf(12_000L));
        return product;
    }

    public static Product product_콜라() {
        Product product = new Product(6L, "콜라", BigDecimal.valueOf(2_000L));
        return product;
    }

    public static MenuProduct menuProduct_포테이토_피자_1_개() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(10L);
        menuProduct.setProductId(5L);
        menuProduct.setQuantity(1);
        menuProduct.setSeq(1L);
        return menuProduct;
    }

    public static MenuProduct menuProduct_콜라_1_개() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(10L);
        menuProduct.setProductId(6L);
        menuProduct.setQuantity(1);
        menuProduct.setSeq(2L);
        return menuProduct;
    }

    public static Menu menu_포테이토_피자_세트(MenuProduct menuProduct1, MenuProduct menuProduct2, MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setId(10L);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(Lists.newArrayList(menuProduct1, menuProduct2));
        menu.setName("포테이토 피자 세트");
        menu.setPrice(BigDecimal.valueOf(13000L));
        return menu;
    }

    public static Product product_파스타() {
        Product product = new Product(3L, "파스타", BigDecimal.valueOf(8_000L));
        return product;
    }

    public static MenuProduct menuProduct_파스타_1_개(Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setQuantity(1);
        menuProduct.setMenuId(11L);
        menuProduct.setProductId(product.getId());
        return menuProduct;
    }

    public static MenuGroup menuGroup_양식() {
        MenuGroup menuGroup = new MenuGroup(2L, "양식");
        return menuGroup;
    }

    public static Menu menu_파스타(MenuProduct menuProduct, MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.setId(11L);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(Lists.newArrayList(menuProduct));
        menu.setName("파스타");
        menu.setPrice(BigDecimal.valueOf(8000L));
        return menu;
    }

    public static OrderLineItem orderLineItem_파스타_2_개(Menu menu) {
        OrderLineItem orderItem = new OrderLineItem();
        orderItem.setMenuId(menu.getId());
        orderItem.setOrderId(1L);
        orderItem.setQuantity(2);
        orderItem.setSeq(1L);
        return orderItem;
    }

    public static Order order_파스타_2_개(OrderLineItem orderItem) {
        Order order = new Order();
        order.setId(1L);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Lists.newArrayList(orderItem));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(7L);
        return order;
    }

    public static OrderTable orderTable_2_명(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(7L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable orderTable_4_명(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(8L);
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup tableGroup_2_개(OrderTable orderTable1, OrderTable orderTable2) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(20L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Lists.newArrayList(orderTable1, orderTable2));
        return tableGroup;
    }
}
