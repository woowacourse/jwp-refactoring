package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends MvcTest {

    @MockBean
    private OrderService orderService;

    @DisplayName("/api/orders로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(orderService.create(any())).willReturn(ORDER_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_1);
        MvcResult mvcResult = postAction("/api/orders", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/orders/1"))
            .andReturn();

        Order orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(ORDER_1);
    }

    @DisplayName("/api/orders로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(orderService.list()).willReturn(Arrays.asList(ORDER_1, ORDER_2));

        MvcResult mvcResult = getAction("/api/orders")
            .andExpect(status().isOk())
            .andReturn();

        List<Order> ordersResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Order>>() {});
        assertAll(
            () -> assertThat(ordersResponse.size()).isEqualTo(2),
            () -> assertThat(ordersResponse.get(0)).usingRecursiveComparison().isEqualTo(ORDER_1),
            () -> assertThat(ordersResponse.get(1)).usingRecursiveComparison().isEqualTo(ORDER_2)
        );
    }

    @DisplayName("/api/orders/{orderId}/order-status로 PUT요청 성공 테스트")
    @Test
    void changeOrderStatusTest() throws Exception {
        given(orderService.changeOrderStatus(anyLong(), any())).willReturn(ORDER_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_1);
        MvcResult mvcResult = putAction("/api/orders/1/order-status", inputJson)
            .andExpect(status().isOk())
            .andReturn();

        Order orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(ORDER_1);
    }
}