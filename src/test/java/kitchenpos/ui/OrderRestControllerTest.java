package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void create() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        OrderTable orderTable2 = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable persistOrderTable = orderTableDao.save(orderTable);
        OrderTable persistOrderTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(persistOrderTable, persistOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroupDao.save(tableGroup);

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("product1");
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(10);
        menuProduct.setProductId(persistProduct.getId());

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setName("menu1");
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(persistMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(persistMenu.getId());

        Order request = new Order();
        request.setOrderTableId(persistOrderTable.getId());
        request.setOrderLineItems(Arrays.asList(orderLineItem));

        String response = mockMvc.perform(post("/api/orders")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Order responseOrder = mapper.readValue(response, Order.class);

        assertAll(
            () -> assertThat(responseOrder).isEqualToComparingOnlyGivenFields(request, "orderTableId"),
            () -> assertThat(responseOrder.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(responseOrder.getOrderedTime()).isNotNull(),
            () -> assertThat(responseOrder.getId()).isNotNull(),
            () -> assertThat(responseOrder.getOrderLineItems()).usingElementComparatorIgnoringFields("seq", "orderId")
                .containsAll(request.getOrderLineItems())
        );
    }

    @Test
    void list() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        OrderTable orderTable2 = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable persistOrderTable = orderTableDao.save(orderTable);
        OrderTable persistOrderTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(persistOrderTable, persistOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroupDao.save(tableGroup);

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("product1");
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(10);
        menuProduct.setProductId(persistProduct.getId());

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setName("menu1");
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(persistMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(persistMenu.getId());

        Order order = new Order();
        order.setOrderStatus("COOKING");
        order.setOrderedTime(LocalDateTime.of(2020, 1, 1, 1, 1));
        order.setOrderTableId(persistOrderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        Order persistOrder = orderDao.save(order);

        String response = mockMvc.perform(get("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<Order> responseOrders = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, Order.class));

        assertThat(responseOrders).usingElementComparatorIgnoringFields("orderLineItems").contains(persistOrder);
    }

    @Test
    void changeOrderStatus() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        OrderTable orderTable2 = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable persistOrderTable = orderTableDao.save(orderTable);
        OrderTable persistOrderTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(persistOrderTable, persistOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroupDao.save(tableGroup);

        Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("product1");
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(10);
        menuProduct.setProductId(persistProduct.getId());

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setName("menu1");
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuGroupId(persistMenuGroup.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(persistMenu.getId());

        Order order = new Order();
        order.setOrderStatus("COOKING");
        order.setOrderedTime(LocalDateTime.of(2020, 1, 1, 1, 1));
        order.setOrderTableId(persistOrderTable.getId());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        Order persistOrder = orderDao.save(order);

        order.setOrderStatus("MEAL");

        String url = String.format("/api/orders/%d/order-status", persistOrder.getId());

        String response = mockMvc.perform(put(url)
            .content(mapper.writeValueAsString(order))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        Order responseOrder = mapper.readValue(response, Order.class);

        assertThat(responseOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }
}