package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.order.request.OrderRequest;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.ui.order.OrderRestController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static kitchenpos.fixture.OrderFixture.createOrderWithId;
import static kitchenpos.fixture.OrderFixture.createOrderWithOrderStatus;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemWithId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
@AutoConfigureMockMvc
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        Order order = createOrderWithId(1L);
        OrderLineItem orderLineItemWithId = createOrderLineItemWithId(2L);
        orderResponse = OrderResponse.of(order, Arrays.asList(orderLineItemWithId));
    }

    @DisplayName("Order 생성 요청")
    @Test
    void create() throws Exception {
        String content = new ObjectMapper().writeValueAsString(new OrderRequest());
        given(orderService.create(any())).willReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/orders/1"));
    }

    @DisplayName("Order 전체 조회 요청")
    @Test
    void list() throws Exception {
        given(orderService.list())
                .willReturn(Arrays.asList(orderResponse));

        mockMvc.perform(get("/api/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)));
    }

    @DisplayName("Order Status 변경 요청")
    @Test
    void changeOrderStatus() throws Exception {
        String content = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(createOrderWithOrderStatus(OrderStatus.MEAL));

        given(orderService.changeOrderStatus(anyLong(), any()))
                .willReturn(createOrderWithOrderStatus(OrderStatus.COMPLETION));

        mockMvc.perform(put("/api/orders/{orderId}/order-status", "1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", Matchers.is("COMPLETION")));
    }
}