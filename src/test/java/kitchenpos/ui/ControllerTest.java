package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("classpath:truncate.sql")
public class ControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T create(T t, String endPoint) throws Exception {
        String body = objectMapper.writeValueAsString(t);
        String result = mockMvc.perform(post(endPoint)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return (T) objectMapper.readValue(result, t.getClass());
    }

    protected <T> T modify(T t, String endPoint) throws Exception {
        String body = objectMapper.writeValueAsString(t);
        String result = mockMvc.perform(put(endPoint)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return (T) objectMapper.readValue(result, t.getClass());
    }

    protected <T> void remove(T t, String endPoint) throws Exception {
        String body = objectMapper.writeValueAsString(t);
        mockMvc.perform(delete(endPoint)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    protected <T> List<T> list(Class<T[]> tClass, String endPoint) throws Exception {
        String result = mockMvc.perform(get(endPoint))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return Arrays.asList(objectMapper.readValue(result, tClass));
    }

    protected MenuGroup menuGroup(String name) throws Exception {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return create(menuGroup, "/api/menu-groups");
    }

    protected List<MenuGroup> menuGroups() throws Exception {
        return list(MenuGroup[].class, "/api/menu-groups");
    }

    protected Menu menu(String name, Long price, MenuGroup menuGroup, List<MenuProduct> products) throws Exception {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(products);
        return create(menu, "/api/menus");
    }

    protected List<Menu> menus() throws Exception {
        return list(Menu[].class, "/api/menus");
    }

    protected OrderLineItem orderLineItem(Menu menu, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    protected Order order(List<OrderLineItem> lineItems, OrderTable table) throws Exception {
        Order order = new Order();
        order.setOrderLineItems(lineItems);
        order.setOrderTableId(table.getId());
        return create(order, "/api/orders");
    }

    protected OrderLineItem orderLineItem() throws Exception {
        MenuGroup chickenCombo = menuGroup("Chicken Combo");
        MenuProduct friedChicken = menuProduct(product("Fried Chicken", 14000L), 1);
        MenuProduct coke = menuProduct(product("Coke", 1000L), 1);
        Menu friedChickenCombo = menu("Fried Chicken Combo", 14500L, chickenCombo, Arrays.asList(friedChicken, coke));
        return orderLineItem(friedChickenCombo, 1);
    }

    protected List<Order> orders() throws Exception {
        return list(Order[].class, "/api/orders");
    }

    protected Order changeStatus(Order order) throws Exception {
        return modify(order, "/api/orders/" + order.getId() + "/order-status");
    }

    protected Product product(String name, Long price) throws Exception {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return create(product, "/api/products");
    }

    protected List<Product> products() throws Exception {
        return list(Product[].class, "/api/products");
    }

    protected MenuProduct menuProduct(Product product, int quantity) throws Exception {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    protected TableGroup tableGroup(List<OrderTable> tables) throws Exception {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tables);
        return create(tableGroup, "/api/table-groups");
    }

    protected void ungroup(TableGroup tableGroup) throws Exception {
        remove(tableGroup, "/api/table-groups/" + tableGroup.getId());
    }

    protected OrderTable table() throws Exception {
        OrderTable orderTable = new OrderTable();
        return create(orderTable, "/api/tables");
    }

    protected OrderTable changeEmpty(OrderTable table) throws Exception {
        return modify(table, "/api/tables/" + table.getId() + "/empty");
    }

    protected OrderTable changeGuests(OrderTable table) throws Exception {
        return modify(table, "/api/tables/" + table.getId() + "/number-of-guests");
    }

    protected List<OrderTable> tables() throws Exception {
        return list(OrderTable[].class, "/api/tables");
    }
}
