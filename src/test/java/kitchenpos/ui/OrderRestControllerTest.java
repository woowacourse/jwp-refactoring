package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
public class OrderRestControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(1L, 1L, OrderStatus.COOKING.name(), null, null);
    }

    @Test
    @DisplayName("POST /api/orders - 주문 생성")
    public void create() throws Exception {
        //given
        given(orderService.create(any(Order.class))).willReturn(order);

        //when & then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("orderTableId").value(1L))
                .andExpect(jsonPath("orderStatus").value(OrderStatus.COOKING.name()));
    }

    @Test
    @DisplayName("GET /api/orders - 주문 목록 조회")
    public void list() throws Exception {
        //given
        given(orderService.list()).willReturn(List.of(order));

        //when & then
        mockMvc.perform(
                        get("/api/orders")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderTableId").value(1L))
                .andExpect(jsonPath("$[0].orderStatus").value(OrderStatus.COOKING.name()));
    }

    @Test
    @DisplayName("PUT /api/orders/{orderId}/order-status - 주문 상태 변경")
    public void changeOrderStatus() throws Exception {
        //given
        final Order mealOrder = new Order(1L, 1L, OrderStatus.MEAL.name(), null, null);
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(mealOrder);

        //when & then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealOrder)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("orderTableId").value(1L))
                .andExpect(jsonPath("orderStatus").value(OrderStatus.MEAL.name()));
    }
}
