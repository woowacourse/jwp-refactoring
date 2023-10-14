package kitchenpos.api.order;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderCreateApiTest extends ApiTestConfig {

    @DisplayName("주문 생성 API 테스트")
    @Test
    void createOrder() throws Exception {
        // given
        final String request = "{\n" +
                "  \"orderTableId\": 1,\n" +
                "  \"orderLineItems\": [\n" +
                "    {\n" +
                "      \"menuId\": 1,\n" +
                "      \"quantity\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long expectedId = 1L;
        final Order expectedOrder = new Order();
        expectedOrder.setId(expectedId);
        when(orderService.create(any(Order.class))).thenReturn(expectedOrder);

        // then
        mockMvc.perform(post("/api/orders")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/orders/%d", expectedId)));
    }
}
