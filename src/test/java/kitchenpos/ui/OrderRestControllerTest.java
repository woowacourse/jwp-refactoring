package kitchenpos.ui;

import kitchenpos.RestControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends RestControllerTest {

    @MockBean
    private OrderService mockOrderService;

    @DisplayName("주문 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        Order requestOrder = createOrder();
        when(mockOrderService.create(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(post("/api/orders")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOrder))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + requestOrder.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(requestOrder)));
    }

    @DisplayName("주문 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<Order> expected = Collections.singletonList(createOrder());
        when(mockOrderService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @DisplayName("주문 상태 변경 요청을 처리한다.")
    @Test
    void changeOrderStatus() throws Exception {
        Long orderId = 1L;
        Order requestOrder = createOrder();
        when(mockOrderService.changeOrderStatus(any(), any())).then(AdditionalAnswers.returnsSecondArg());
        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestOrder))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestOrder)));
    }
}
