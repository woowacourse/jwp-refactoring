package kitchenpos.order.ui;

import static java.time.LocalDateTime.now;
import static kitchenpos.order.domain.type.OrderStatus.COOKING;
import static kitchenpos.util.ObjectCreator.getObject;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.request.ChangeOrderRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성한다")
    @Test
    void create() throws Exception {
        // given
        final CreateOrderRequest request = getObject(
                CreateOrderRequest.class,
                1L,
                List.of()
        );

        final Order order = getObject(Order.class, 1L, 1L, COOKING, now(), List.of());
        when(orderService.create(any()))
                .thenReturn(OrderResponse.from(order));

        // when & then
        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("orderTableId").isNumber())
                .andExpect(jsonPath("orderStatus").isString())
                .andExpect(jsonPath("orderedTime").isString())
                .andExpect(jsonPath("orderLineItems").isArray());
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() throws Exception {
        // given
        final Order order = getObject(Order.class, 1L, 1L, COOKING, now(), List.of());

        when(orderService.list()).thenReturn(List.of(OrderResponse.from(order)));

        // when & then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        final ChangeOrderRequest request = getObject(ChangeOrderRequest.class, "COOKING");
        final Order order = getObject(Order.class, 1L, 1L, COOKING, now(), List.of());

        when(orderService.changeOrderStatus(any(), any()))
                .thenReturn(OrderResponse.from(order));

        // when & then
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("orderTableId").isNumber())
                .andExpect(jsonPath("orderStatus").isString())
                .andExpect(jsonPath("orderedTime").isString())
                .andExpect(jsonPath("orderLineItems").isArray());
    }
}
