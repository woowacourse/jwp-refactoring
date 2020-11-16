package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.OrderChangeStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.TableFixture;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = OrderFixture.createWithId(OrderFixture.ID1, OrderFixture.MEAL_STATUS,
            TableFixture.ID1);
    }

    @DisplayName("Order 생성")
    @Test
    void create() throws Exception {
        when(orderService.create(any(OrderCreateRequest.class))).thenReturn(
            OrderResponse.of(order));

        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(order))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("location", "/api/orders/" + order.getId()))
            .andExpect(jsonPath("id").value(order.getId()));
    }

    @DisplayName("Find all order")
    @Test
    void list() throws Exception {
        when(orderService.list()).thenReturn(Collections.singletonList(OrderResponse.of(order)));

        mockMvc.perform(get("/api/orders")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").value(order.getId()));
    }

    @DisplayName("Order Status 수정")
    @Test
    void changeOrderStatus() throws Exception {
        OrderResponse response = OrderResponse.of(
            OrderFixture.createWithId(OrderFixture.ID1, OrderFixture.COOKING_STATUS,
                TableFixture.ID1));
        OrderChangeStatusRequest request = OrderFixture.changeStatusRequest(
            OrderStatus.COOKING);
        when(orderService.changeOrderStatus(anyLong(),
            any(OrderChangeStatusRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("orderStatus").value(response.getOrderStatus()));
    }
}
