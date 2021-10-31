package kitchenpos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import org.springframework.beans.factory.annotation.Autowired;

public class Constructor {

    @Autowired
    private ObjectMapper objectMapper;

    protected MenuGroup menuGroupConstructor(final Long id, final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }

    protected MenuGroup menuGroupConstructor(final String name) {
        return menuGroupConstructor(null, name);
    }

    protected Menu menuConstructor(final String name, final int price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        return menuConstructor(null, name, new BigDecimal(price), menuGroupId, menuProducts);
    }

    protected Menu menuConstructor(final Long id, final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    protected Order orderConstructor(final OrderStatus orderStatus) {
        return orderConstructor(null, null, orderStatus.name(), LocalDateTime.now(), null);
    }

    protected Order orderConstructor(final List<OrderLineItem> orderLineItems) {
        return orderConstructor(null, null, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    protected Order orderConstructor(final Long id, final List<OrderLineItem> orderLineItems) {
        return orderConstructor(id, null, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
    }

    protected Order orderConstructor(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
        final List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    protected OrderLineItem orderLineItemConstructor(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    protected Product productConstructor(final String name, final int price) {
        return productConstructor(null, name, price);
    }

    protected Product productConstructor(final Long id, final String name, final int price) {
        return productConstructor(id, name, new BigDecimal(price));
    }

    protected Product productConstructor(final Long id, final String name, final BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    protected TableGroup tableGroupConstructor(final List<OrderTable> orderTables) {
        return tableGroupConstructor(null, LocalDateTime.now(), orderTables);
    }

    protected TableGroup tableGroupConstructor(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    protected OrderTable orderTableConstructor(final int numberOfGuests) {
        return orderTableConstructor(null, null, numberOfGuests, true);
    }

    protected OrderTable orderTableConstructor(final int numberOfGuests, final boolean empty) {
        return orderTableConstructor(null, null, numberOfGuests, empty);
    }

    protected OrderTable orderTableConstructor(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    protected MenuProduct menuProductConstructor(final Long seq, final Long menuId, final Long productId, final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    protected String objectToJson(final Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
