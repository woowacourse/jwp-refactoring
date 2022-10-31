package kitchenpos.ui;

import static kitchenpos.OrderFixtures.createOrderResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.OrderFixtures;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

    @Test
    void create() throws Exception {
        // given
        OrderResponse response = createOrderResponse();
        given(orderService.create(any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(post("/api/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(OrderFixtures.createOrderRequest()))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + response.getId()));
    }

    @Test
    void list() throws Exception {
        // given
        OrderResponse response = createOrderResponse();
        given(orderService.list()).willReturn(List.of(response));

        // when
        ResultActions actions = mockMvc.perform(get("/api/orders"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(response))));
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        OrderResponse response = createOrderResponse();
        given(orderService.changeOrderStatus(any(), any())).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(OrderFixtures.createOrderChangeRequest()))
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
