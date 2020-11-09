package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderMenuRequest;
import kitchenpos.dto.OrderStatusChangeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends MvcTest {

    @MockBean
    private OrderService orderService;

    @DisplayName("/api/orders로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(orderService.create(any())).willReturn(ORDER_1);

        OrderMenuRequest orderMenuRequest = new OrderMenuRequest(MENU_ID_1, ORDER_MENU_QUANTITY_1);
        OrderCreateRequest orderCreateRequest =
            new OrderCreateRequest(Arrays.asList(orderMenuRequest), TABLE_ID_1);

        String inputJson = objectMapper.writeValueAsString(orderCreateRequest);
        MvcResult mvcResult = postAction("/api/orders", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/orders/%d", ORDER_ID_1)))
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

        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COOKING);
        String inputJson = objectMapper.writeValueAsString(orderStatusChangeRequest);
        MvcResult mvcResult = putAction(String.format("/api/orders/%d/order-status", ORDER_ID_1), inputJson)
            .andExpect(status().isOk())
            .andReturn();

        Order orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(ORDER_1);
    }
}