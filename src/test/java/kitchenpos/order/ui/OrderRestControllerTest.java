package kitchenpos.order.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreationRequest;
import kitchenpos.order.application.dto.OrderItemsWithQuantityRequest;
import kitchenpos.order.application.dto.OrderResult;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void create() throws Exception {
        // given
        final OrderResult result = new OrderResult(1L, "COOKING", null);
        given(orderService.create(any())).willReturn(result);

        final OrderCreationRequest request = new OrderCreationRequest(1L, List.of(
                new OrderItemsWithQuantityRequest(1L, 1L),
                new OrderItemsWithQuantityRequest(2L, 2L)
        ));

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/orders/1"));
    }

    @Test
    void list() throws Exception {
        // given
        final OrderResult resultA = new OrderResult(1L, "COOKING", null);
        final OrderResult resultB = new OrderResult(2L, "COOKING", null);
        given(orderService.list()).willReturn(List.of(resultA, resultB));

        // when
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultA, resultB))));
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        final OrderResult result = new OrderResult(1L, "COOKING", null);
        given(orderService.changeOrderStatus(any(), any())).willReturn(result);

        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL);

        // when
        mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(result)));
    }
}
