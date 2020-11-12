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
import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;
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
    private TableDao tableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderService orderService;

    @Test
    void create() throws Exception {
        Table table = new Table(5, false);
        Table table2 = new Table(10, false);

        Table persistTable = tableDao.save(table);
        Table persistTable2 = tableDao.save(table2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(persistTable, persistTable2));
        tableGroupDao.save(tableGroup);

        Product product = new Product("product1", BigDecimal.TEN);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct(null, persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu("menu1", new Price(BigDecimal.TEN), persistMenuGroup.getId(), Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(persistMenu.getId(), 1L);

        OrderCreateRequest request = new OrderCreateRequest(persistTable.getId(), Arrays.asList(orderLineItem));

        String response = mockMvc.perform(post("/api/orders")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        OrderResponse responseOrder = mapper.readValue(response, OrderResponse.class);

        assertAll(
            () -> assertThat(responseOrder).isEqualToComparingOnlyGivenFields(request, "orderTableId"),
            () -> assertThat(responseOrder.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(responseOrder.getOrderedTime()).isNotNull(),
            () -> assertThat(responseOrder.getId()).isNotNull(),
            () -> assertThat(responseOrder.getOrderLineItems().get(0).getMenuId())
                .isEqualTo(request.getOrderLineItems().get(0).getMenuId()),
            () -> assertThat(responseOrder.getOrderLineItems().get(0).getQuantity())
                .isEqualTo(request.getOrderLineItems().get(0).getQuantity())
        );
    }

    @Test
    void list() throws Exception {
        Table table = new Table(5, false);
        Table table2 = new Table(10, false);

        Table persistTable = tableDao.save(table);
        Table persistTable2 = tableDao.save(table2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(persistTable, persistTable2));
        tableGroupDao.save(tableGroup);

        Product product = new Product("product1", BigDecimal.TEN);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct(null, persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu("menu1", new Price(BigDecimal.TEN), persistMenuGroup.getId(), Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(persistMenu.getId(), 1L);

        OrderCreateRequest order = new OrderCreateRequest(persistTable.getId(), Arrays.asList(orderLineItem));
        OrderResponse persistOrder = orderService.create(order);

        String response = mockMvc.perform(get("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<OrderResponse> responseOrders = mapper.readValue(response, mapper.getTypeFactory()
            .constructCollectionType(List.class, OrderResponse.class));

        assertThat(responseOrders).usingElementComparatorIgnoringFields("orderLineItems").contains(persistOrder);
    }

    @Test
    void changeOrderStatus() throws Exception {
        Table table = new Table(5, false);
        Table table2 = new Table(10, false);

        Table persistTable = tableDao.save(table);
        Table persistTable2 = tableDao.save(table2);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(persistTable, persistTable2));
        tableGroupDao.save(tableGroup);

        Product product = new Product("product1", BigDecimal.TEN);
        Product persistProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct(null, persistProduct.getId(), 10);

        MenuGroup menuGroup = new MenuGroup("menuGroup1");
        MenuGroup persistMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu("menu1", new Price(BigDecimal.TEN), persistMenuGroup.getId(), Arrays.asList(menuProduct));
        Menu persistMenu = menuDao.save(menu);

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(persistMenu.getId(), 1L);

        OrderCreateRequest order = new OrderCreateRequest(persistTable.getId(), Arrays.asList(orderLineItem));
        OrderResponse persistOrder = orderService.create(order);

        OrderStatusChangeRequest request = new OrderStatusChangeRequest("MEAL");

        String url = String.format("/api/orders/%d/order-status", persistOrder.getId());

        String response = mockMvc.perform(put(url)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        OrderResponse responseOrder = mapper.readValue(response, OrderResponse.class);

        assertThat(responseOrder.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }
}