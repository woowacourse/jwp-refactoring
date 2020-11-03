package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @DisplayName("주문 생성 요청 테스트")
    @Test
    void create() throws Exception {
        List<OrderLineItemDto> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemDto(1L, 1L, 1L, 2));
        OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.COOKING, orderLineItems, LocalDateTime.now());

        given(orderService.create(any())).willReturn(orderResponse);

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
                .andExpect(header().string("Location", "/api/orders/" + orderResponse.getId()))
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderTableId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderedTime", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.orderLineItems[0].orderId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderLineItems[0].menuId", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.orderLineItems[0].quantity", Matchers.instanceOf(Number.class)));
    }

    @DisplayName("주문 생성 요청 테스트 - orderLineItems가 비어있을 때 예외처리")
    @Test
    void createWhenEmptyOrderLineItems() throws Exception {
        mockMvc.perform(post("/api/orders")
                .content("{\n"
                        + "  \"orderTableId\": 1,\n"
                        + "  \"orderLineItems\": [\n"
                        + "  ]\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Product 목록 조회 요청 테스트")
    @Test
    void list() throws Exception {
        List<OrderLineItemDto> orderLineItems = new ArrayList<>();
        List<OrderResponse> orders = new ArrayList<>();
        orders.add(new OrderResponse(1L, 1L, OrderStatus.COOKING, orderLineItems, LocalDateTime.now()));
        orders.add(new OrderResponse(2L, 2L, OrderStatus.COOKING, orderLineItems, LocalDateTime.now()));

        given(orderService.list()).willReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @DisplayName("주문 상태 변경 요청 테스트")
    @Test
    void changeOrderStatus() throws Exception {
        OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.MEAL, new ArrayList<>(), LocalDateTime.now());

        given(orderService.changeOrderStatus(eq(1L), any())).willReturn(orderResponse);

        mockMvc.perform(put("/api/orders/1/order-status")
                .content("{\n"
                        + "  \"orderStatus\": \"MEAL\"\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", Matchers.is(OrderStatus.MEAL.name())));
    }

    @DisplayName("주문 상태 변경 요청 테스트 - orderStatus가 없을 때")
    @Test
    void changeOrderStatusWhenBlankOrderStatusInRequest() throws Exception {
        mockMvc.perform(put("/api/orders/1/order-status")
                .content("{\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
