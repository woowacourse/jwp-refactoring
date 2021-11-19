package kitchenpos.fixture;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Fixtures {

    public static MenuProduct menuProduct(long seq,
                                          long menuId,
                                          long productId,
                                          long quantity) {

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Menu menu(Long id,
                            String name,
                            Long price,
                            Long menuGroupId,
                            List<MenuProduct> menuProducts) {

        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        if (price != null) {
            menu.setPrice(BigDecimal.valueOf(price));
        }
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuGroup menuGroup(
            long id,
            String name
    ) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Product product(
            long id,
            String name,
            long price
    ) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static Order order(
            long id,
            long orderTableId,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderLineItem orderLineItem(
            long seq,
            long orderId,
            long menuId,
            long quantity
    ) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderStatus orderStatus() {
        return null;
    }

    public static OrderTable orderTable(
            long id,
            long tableGroupId,
            int numberOfGuests,
            boolean empty
    ) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup tableGroup(
            long id,
            LocalDateTime createdDate,
            List<OrderTable> orderTables
    ) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
