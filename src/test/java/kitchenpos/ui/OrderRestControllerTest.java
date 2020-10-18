package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
                        + "      \"quantity\": 2\n"
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

    @DisplayName("Product 목록 조회 요청 테스트")
    @Test
    void list() throws Exception {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(new OrderTable(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));
        orders.add(new Order(new OrderTable(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems));

        given(orderService.list()).willReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}
