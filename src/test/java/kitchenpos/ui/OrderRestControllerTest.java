package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.application.dto.response.CreateOrderResponse;
import kitchenpos.application.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 주문_생성() throws Exception {
            // given
            CreateOrderResponse response = RESPONSE.주문_생성_응답_조리중();
            given(orderService.create(any(CreateOrderRequest.class)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/orders")
                            .contentType("application/json")
                            .content("{" +
                                    "\"orderTableId\":1," +
                                    "\"orderLineItems\":" +
                                    "[" +
                                    "{" +
                                    "\"menuId\":1," +
                                    "\"quantity\":1" +
                                    "}" +
                                    "]" +
                                    "}"))
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").value(response.getId()),
                            jsonPath("orderTableId").value(response.getOrderTableId()),
                            jsonPath("orderStatus").value(response.getOrderStatus()),
                            jsonPath("orderLineItems[0].seq").value(response.getOrderLineItems().get(0).getSeq()),
                            jsonPath("orderLineItems[0].menuId").value(response.getOrderLineItems().get(0).getMenuId()),
                            jsonPath("orderLineItems[0].quantity").value(response.getOrderLineItems().get(0).getQuantity())
                    );
        }

        @Test
        void 주문_목록_조회() throws Exception {
            // given
            List<OrderResponse> orders = List.of(RESPONSE.주문_조리중_응답());
            given(orderService.list())
                    .willReturn(orders);

            // when & then
            mockMvc.perform(get("/api/orders"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$[0].id").value(orders.get(0).getId()),
                            jsonPath("$[0].orderTableId").value(orders.get(0).getOrderTableId()),
                            jsonPath("$[0].orderStatus").value(orders.get(0).getOrderStatus()),
                            jsonPath("$[0].orderedTime").value(orders.get(0).getOrderedTime()),
                            jsonPath("$[0].orderLineItems[0].seq").value(orders.get(0).getOrderLineItems().get(0).getSeq()),
                            jsonPath("$[0].orderLineItems[0].menuId").value(orders.get(0).getOrderLineItems().get(0).getMenuId()),
                            jsonPath("$[0].orderLineItems[0].quantity").value(orders.get(0).getOrderLineItems().get(0).getQuantity())
                    );
        }

        @Test
        void 주문_상태_수정() throws Exception {
            // given
            OrderResponse response = RESPONSE.주문_식사중_응답();
            given(orderService.changeOrderStatus(anyLong(), any(UpdateOrderStatusRequest.class)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(put("/api/orders/1/order-status")
                            .contentType(APPLICATION_JSON)
                            .content("{" +
                                    "\"orderStatus\":\"COMPLETION\"" +
                                    "}"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(response.getId()),
                            jsonPath("orderStatus").value(response.getOrderStatus()),
                            jsonPath("orderTableId").value(response.getOrderTableId())
                    );

        }
    }
}
