package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(DataClearExtension.class)
public class ControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    public Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return restTemplate.postForEntity("/api/products", product, Product.class).getBody();
    }

    public MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return restTemplate.postForEntity("/api/menu-groups", menuGroup, MenuGroup.class).getBody();
    }

    public Menu createMenu(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return restTemplate.postForEntity("/api/menus", menu, Menu.class).getBody();
    }

    public Order createOrder(long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return restTemplate.postForEntity("/api/orders", order, Order.class).getBody();
    }

    public void updateOrder(long orderId, String orderStatus) {
        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(orderStatus);
        restTemplate.put("/api/orders/{orderId}/order-status", order, orderId);
    }

    public OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return restTemplate.postForEntity("/api/tables", orderTable, OrderTable.class).getBody();
    }

    public TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return restTemplate.postForEntity("/api/table-groups", tableGroup, TableGroup.class).getBody();
    }
}
