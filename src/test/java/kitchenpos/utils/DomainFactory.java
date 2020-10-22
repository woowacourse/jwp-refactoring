package kitchenpos.utils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

public class DomainFactory {

    private DomainFactory() {
    }

    public static Menu createMenu(Long id, BigDecimal price,
                                  Long menuGroupId, MenuProduct menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }

    public static MenuProduct createMenuProduct(Long seq, Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static Product createProduct(Long id, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setPrice(price);
        return product;
    }

    public static OrderTable createOrderTable(Long id, int numberOfGuests, Long tableGroupId, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static Order createOrder(Long id, String orderStatus, Long tableId, LocalDateTime orderedTime) {
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(orderStatus);
        order.setOrderTableId(tableId);
        order.setOrderedTime(orderedTime);
        return order;
    }
}
