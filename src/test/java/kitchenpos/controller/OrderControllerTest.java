package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
public class OrderControllerTest extends ControllerTest {

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        Order order = new Order(1L, List.of(new OrderLineItem(1L, 1)));
        given(orderService.create(any()))
                .willReturn(new Order(1L, 1L, "MEAL", LocalDateTime.now(), List.of()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("주문을 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    public void changeOrderStatus() throws Exception {
        // given
        Order order = new Order(1L, 1L, "MEAL", LocalDateTime.now(), List.of());

        // when
        ResultActions perform = mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
