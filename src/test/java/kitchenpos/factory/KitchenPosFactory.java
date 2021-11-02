package kitchenpos.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

public class KitchenPosFactory {

    private KitchenPosFactory() {}

    public static MenuGroup getStandardMenuGroup() {
        MenuGroup standardMenuGroup = new MenuGroup();
        standardMenuGroup.setName("메뉴그룹이름");
        standardMenuGroup.setId(1L);
        return standardMenuGroup;
    }

    public static List<MenuGroup> getStandardMenuGroups() {
        List<MenuGroup> standardMenuGroups = new ArrayList<>();
        standardMenuGroups.add(getStandardMenuGroup());
        return standardMenuGroups;
    }

    public static Product getStandardProduct() {
        Product standardProduct = new Product();
        standardProduct.setId(1L);
        standardProduct.setName("상품 이름");
        standardProduct.setPrice(new BigDecimal(1000));
        return standardProduct;
    }

    public static List<Product> getStandardProducts() {
        List<Product> standardProducts = new ArrayList<>();
        standardProducts.add(getStandardProduct());
        return standardProducts;
    }

    public static MenuProduct getStandardMenuProduct() {
        MenuProduct standardMenuProduct = new MenuProduct();
        standardMenuProduct.setProductId(1L);
        standardMenuProduct.setMenuId(1L);
        standardMenuProduct.setSeq(1L);
        standardMenuProduct.setQuantity(1L);
        return standardMenuProduct;
    }

    public static List<MenuProduct> getStandardMenuProducts() {
        List<MenuProduct> standardMenuProducts = new ArrayList<>();
        standardMenuProducts.add(getStandardMenuProduct());
        return standardMenuProducts;
    }

    public static Menu getStandardMenu() {
        Menu standardMenu = new Menu();
        standardMenu.setName("메뉴이름");
        standardMenu.setId(1L);
        standardMenu.setPrice(new BigDecimal(1000));
        standardMenu.setMenuGroupId(1L);
        standardMenu.setMenuProducts(getStandardMenuProducts());
        return standardMenu;
    }

    public static List<Menu> getStandardMenus() {
        List<Menu> standardMenus = new ArrayList<>();
        standardMenus.add(getStandardMenu());
        return standardMenus;
    }

    public static Order getStandardOrder() {
        Order standardOrder = new Order();
        standardOrder.setId(1L);
        standardOrder.setOrderTableId(1L);
        standardOrder.setOrderedTime(LocalDateTime.now());
        standardOrder.setOrderLineItems(getStandardOrderLineItems());
        standardOrder.setOrderStatus(OrderStatus.COOKING.name());
        return standardOrder;
    }

    public static List<Order> getStandardOrders() {
        List<Order> standardOrders = new ArrayList<>();
        standardOrders.add(getStandardOrder());
        return standardOrders;
    }

    public static OrderLineItem getStandardOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        orderLineItem.setSeq(1L);
        return orderLineItem;
    }

    public static List<OrderLineItem> getStandardOrderLineItems() {
        List<OrderLineItem> standardOrderLineItems = new ArrayList<>();
        standardOrderLineItems.add(getStandardOrderLineItem());
        return standardOrderLineItems;
    }

    public static OrderTable getStandardOrderTable() {
        OrderTable standardOrderTable = new OrderTable();
        standardOrderTable.setId(1L);
        standardOrderTable.setTableGroupId(1L);
        standardOrderTable.setEmpty(false);
        standardOrderTable.setNumberOfGuests(1);
        return standardOrderTable;
    }

    public static List<OrderTable> getStandardOrderTables() {
        List<OrderTable> standardOrderTables = new ArrayList<>();
        standardOrderTables.add(getStandardOrderTable());
        return standardOrderTables;
    }

    public static TableGroup getStandardTableGroup() {
        List<OrderTable> standardOrderTables = getStandardOrderTables();
        OrderTable standardOrderTable = getStandardOrderTable();
        standardOrderTable.setId(2L);
        standardOrderTable.setTableGroupId(null);
        standardOrderTable.setEmpty(true);
        standardOrderTables.get(0).setEmpty(true);
        standardOrderTables.get(0).setTableGroupId(null);
        standardOrderTables.add(standardOrderTable);

        TableGroup standardTableGroup = new TableGroup();
        standardTableGroup.setId(1L);

        standardTableGroup.setOrderTables(standardOrderTables);
        standardTableGroup.setCreatedDate(LocalDateTime.now());
        return standardTableGroup;
    }
}
