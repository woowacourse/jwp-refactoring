package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemDto;
import kitchenpos.ui.dto.request.OrderStatusChangeRequest;
import kitchenpos.ui.dto.response.OrderCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(1L, Arrays.asList(new OrderLineItemDto(1L, 2)));
        given(orderService.create(any())).willReturn(OrderCreateResponse.of(Order.builder()
                .id(1L)
                .orderStatus(OrderStatus.COOKING)
                .orderTableId(1L)
                .orderedTime(LocalDateTime.now())
                .build(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("주문을 생성할 때 주문항목이 비어있으면 에러를 반환한다.")
    @Test
    void create_fail_if_orderLineItems_is_empty() throws Exception {
        // given
        OrderCreateRequest request = new OrderCreateRequest(1L, new ArrayList<>());
        given(orderService.create(any())).willReturn(OrderCreateResponse.of(Order.builder()
                .id(1L)
                .orderStatus(OrderStatus.COOKING)
                .orderTableId(1L)
                .orderedTime(LocalDateTime.now())
                .build(), new ArrayList<>()));

        // when
        ResultActions perform = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() throws Exception {
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
    void changeOrderStatus() throws Exception {
        // given
        OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        // when
        ResultActions perform = mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
