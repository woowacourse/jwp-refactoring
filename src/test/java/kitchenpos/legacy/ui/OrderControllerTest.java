package kitchenpos.legacy.ui;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.legacy.application.LegacyOrderService;
import kitchenpos.legacy.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(ReplaceUnderscores.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LegacyOrderService orderService;

    @Test
    @DisplayName("/api/orders로 POST 요청을 보내면 201 응답이 반환된다.")
    void create_with_201() throws Exception {
        // given
        Order request = new Order();
        Order response = new Order();
        response.setId(1L);
        given(orderService.create(any(Order.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("/api/orders/1"));
    }

    @Test
    @DisplayName("/api/orders로 GET 요청을 보내면 200 응답과 결과가 반환된다.")
    void findAll_with_201() throws Exception {
        // given
        given(orderService.findAll())
            .willReturn(List.of(new Order(), new Order()));

        // when & then
        mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("/api/orders/{id}/order-status로 PUT 요청을 보내면 200 응답이 반환된다.")
    void changeOrderStatus_with_200() throws Exception {
        // given
        Long orderId = 1L;
        Order request = new Order();
        Order response = new Order();
        given(orderService.changeOrderStatus(anyLong(), any(Order.class)))
            .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/orders/{id}/order-status", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }
}
