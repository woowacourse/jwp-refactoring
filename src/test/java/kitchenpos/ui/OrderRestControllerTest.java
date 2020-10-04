package kitchenpos.ui;

import kitchenpos.TestObjectFactory;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void create() throws Exception {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(TestObjectFactory.createOrderLineItem(1L, 10L, 1L));

        Order order = TestObjectFactory.createOrder(1L, orderLineItems);
        order.setId(10L);

        given(orderService.create(any())).willReturn(order);

        mockMvc.perform(post("/api/orders")
                .content("{\n"
                        + "  \"orderTableId\": 1,\n"
                        + "  \"orderLineItems\": [\n"
                        + "    {\n"
                        + "      \"menuId\": 1,\n"
                        + "      \"quantity\": 1\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/10"))
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderTableId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderLineItems[0].orderId", Matchers.is(Integer.parseInt(String.valueOf(order.getId())))))
                .andExpect(jsonPath("$.orderLineItems[0].menuId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderLineItems[0].quantity", Matchers.instanceOf(Number.class)));
    }

    @DisplayName("Product 목록 조회 요청 테스트")
    @Test
    void list() throws Exception {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(TestObjectFactory.createOrderLineItem(1L, 10L, 1L));

        List<Order> orders = new ArrayList<>();
        orders.add(TestObjectFactory.createOrder(1L, orderLineItems));
        orders.add(TestObjectFactory.createOrder(2L, orderLineItems));

        given(orderService.list()).willReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}
