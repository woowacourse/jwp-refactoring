package kitchenpos.order.ui;

import static kitchenpos.fixture.OrderFixture.ORDER;
import static kitchenpos.fixture.OrderFixture.ORDER_NON_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void create() throws Exception {
        // given
        given(orderService.create(any())).willReturn(ORDER);

        // when & then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ORDER_NON_ID)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/orders/1"));
    }

    @Test
    void list() throws Exception {
        // given
        given(orderService.list()).willReturn(List.of(ORDER));

        // when & then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(ORDER.getId()))
                .andExpect(jsonPath("$[0].orderTableId").value(ORDER.getOrderTableId()))
                .andExpect(jsonPath("$[0].orderedTime").value(ORDER.getOrderedTime().toString()))
                .andExpect(jsonPath("$[0].orderStatus").value(ORDER.getOrderStatus()))
                .andExpect(jsonPath("$[0].orderLineItems.size()").value(ORDER.getOrderLineItems().size()));
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        given(orderService.changeOrderStatus(any(), any())).willReturn(ORDER);

        // when & then
        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ORDER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ORDER.getId()))
                .andExpect(jsonPath("$.orderTableId").value(ORDER.getOrderTableId()))
                .andExpect(jsonPath("$.orderedTime").value(ORDER.getOrderedTime().toString()))
                .andExpect(jsonPath("$.orderStatus").value(ORDER.getOrderStatus()))
                .andExpect(jsonPath("$.orderLineItems.size()").value(ORDER.getOrderLineItems().size()));

    }
}
