package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.*;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {
    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() throws Exception {
        List<OrderLineItemCreateRequest> orderLineItems =
                Arrays.asList(OrderLineItemFixture.createOrderLineItemRequest(1L, 2), OrderLineItemFixture.createOrderLineItemRequest(2L, 1));
        OrderCreateRequest request = createOrderRequest(orderLineItems, 10L);
        byte[] content = objectMapper.writeValueAsBytes(request);
        Order order = createOrder(5L, COOKING, 10L);
        order.setOrderLineItems(Arrays.asList(
                createOrderLineItem(20L, 5L, 1L, 2),
                createOrderLineItem(21L, 5L, 2L, 1))
        );
        order.setOrderedTime(LocalDateTime.now());
        given(orderService.create(any(OrderCreateRequest.class))).willReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.orderTableId").value(10))
                .andExpect(jsonPath("$.orderLineItems[0].menuId").value(1))
                .andExpect(jsonPath("$.orderLineItems[0].quantity").value(2));
    }

    @Test
    @DisplayName("전체 주문을 조회한다")
    void list() throws Exception {
        HashMap<Long, List<OrderLineItem>> orderLineItems = new HashMap<Long, List<OrderLineItem>>() {{
            put(1L, Arrays.asList(createOrderLineItem(1L, 1L, 1)));
            put(2L, Arrays.asList(createOrderLineItem(2L, 1L, 2), createOrderLineItem(2L, 2L, 1)));
            put(3L, Arrays.asList(createOrderLineItem(3L, 1L, 1), createOrderLineItem(3L, 2L, 2)));
        }};
        List<Order> persistedOrders = Arrays.asList(
                createOrder(1L, COOKING, 5L),
                createOrder(2L, COOKING, 5L),
                createOrder(3L, COOKING, 6L)
        );
        for (Order order : persistedOrders) {
            order.setOrderLineItems(orderLineItems.get(order.getId()));
        }
        given(orderService.list()).willReturn(persistedOrders);

        byte[] responseBody = mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        List<Order> result = objectMapper.readValue(responseBody, new TypeReference<List<Order>>() {
        });

        assertThat(result).usingRecursiveComparison().isEqualTo(persistedOrders);
    }

    @Test
    @DisplayName("주문의 상태를 수정한다")
    void update() throws Exception {
        Order request = updateOrderRequest(OrderStatus.COMPLETION);
        byte[] content = objectMapper.writeValueAsBytes(request);
        Order order = createOrder(5L, COMPLETION, 10L);
        order.setOrderLineItems(Arrays.asList(
                createOrderLineItem(20L, 5L, 1L, 2),
                createOrderLineItem(21L, 5L, 2L, 1))
        );
        order.setOrderedTime(LocalDateTime.now());
        given(orderService.changeOrderStatus(eq(5L), any(Order.class))).willReturn(order);

        mockMvc.perform(put("/api/orders/{id}/order-status", 5L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COMPLETION.name()));
    }
}