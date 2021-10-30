package kitchenpos;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class KitchenPosTestFixture {

    public static final List<String> COOKING_OR_MEAL_STATUS = Arrays.asList(
            OrderStatus.COOKING.name(), OrderStatus.MEAL.name()
    );

    public static Product 상품을_저장한다(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Menu 메뉴를_저장한다(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu savedMenu = new Menu();
        savedMenu.setId(id);
        savedMenu.setName(name);
        savedMenu.setPrice(price);
        savedMenu.setMenuGroupId(menuGroupId);
        savedMenu.setMenuProducts(menuProducts);
        return savedMenu;
    }

    public static Menu 메뉴를_저장한다(long id, Menu menu) {
        return 메뉴를_저장한다(id, menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());
    }

    public static MenuGroup 메뉴_그룹을_저장한다(Long id, String groupName) {
        MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(id);
        savedMenuGroup.setName(groupName);
        return savedMenuGroup;
    }

    public static OrderTable 주문_테이블을_저장한다(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static TableGroup 테이블_그룹을_저장한다(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static MenuProduct 메뉴_상품을_저장한다(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Order 주문을_저장한다(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order 주문을_저장한다(Long id, Order order) {
        return 주문을_저장한다(id, order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems());
    }

    public static OrderLineItem 주문_항목을_저장한다(Long id, Long orderId, Long menuId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(id);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
