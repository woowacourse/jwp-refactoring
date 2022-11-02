package kitchenpos.ui;

import static javax.management.openmbean.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderCreationDto;
import kitchenpos.common.ControllerTest;
import kitchenpos.common.fixture.RequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
@DisplayName("OrderRestController 는 ")
class OrderRestControllerTest extends ControllerTest {

    @MockBean
    OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() throws Exception {
        when(orderService.create(any(OrderCreationDto.class))).thenReturn(DtoFixture.getOrderDto());

        final ResultActions resultActions = mockMvc.perform(post("/api/v2/orders")
                        .content(objectMapper.writeValueAsString(RequestBody.getOrder(1L, 1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andDo(document("order/create",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("orderTableId").type(NUMBER).description("order table id"),
                        fieldWithPath("orderLineItems.[].menuId").type(NUMBER).description("menuId of orderLineItems"),
                        fieldWithPath("orderLineItems.[].quantity").type(NUMBER)
                                .description("quantity of orderLineItems")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("table order id"),
                        fieldWithPath("orderTableId").type(NUMBER).description("order table id"),
                        fieldWithPath("orderStatus").type(STRING).description("order table status"),
                        fieldWithPath("orderedTime").type(STRING).description("order time"),
                        fieldWithPath("orderLineItems.[].id").type(NUMBER).description("id of order line item"),
                        fieldWithPath("orderLineItems.[].orderId").type(NUMBER)
                                .description("order id of order line item"),
                        fieldWithPath("orderLineItems.[].quantity").type(NUMBER)
                                .description("quantity of order lien item"),
                        fieldWithPath("orderLineItems.[].orderedMenuId").type(NUMBER).description("menu id of order line item")
                )
        ));
    }

    @DisplayName("주문 항목을 가져온다.")
    @Test
    void getOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(List.of(DtoFixture.getOrderDto()));

        final ResultActions resultActions = mockMvc.perform(get("/api/v2/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("order/get-orders",
                getResponsePreprocessor(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("table order id"),
                        fieldWithPath("[].orderTableId").type(NUMBER).description("order table id"),
                        fieldWithPath("[].orderStatus").type(STRING).description("order table status"),
                        fieldWithPath("[].orderedTime").type(STRING).description("order time"),
                        fieldWithPath("[].orderLineItems.[].id").type(NUMBER).description("id of order line item"),
                        fieldWithPath("[].orderLineItems.[].orderId").type(NUMBER)
                                .description("order id of order line item"),
                        fieldWithPath("[].orderLineItems.[].quantity").type(NUMBER)
                                .description("quantity of order lien item"),
                        fieldWithPath("[].orderLineItems.[].orderedMenuId").type(NUMBER)
                                .description("menu id of order line item")
                )
        ));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        when(orderService.changeOrderStatus(anyLong(), anyString())).thenReturn(DtoFixture.getOrderDto());

        final ResultActions resultActions = mockMvc.perform(put("/api/v2/orders/1/order-status")
                        .content(objectMapper.writeValueAsString(Map.of("orderStatus", "COOKING")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("order/change-order-status",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("orderStatus").type(STRING).description("order status")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("table order id"),
                        fieldWithPath("orderTableId").type(NUMBER).description("order table id"),
                        fieldWithPath("orderStatus").type(STRING).description("order table status"),
                        fieldWithPath("orderedTime").type(STRING).description("order time"),
                        fieldWithPath("orderLineItems.[].id").type(NUMBER).description("id of order line item"),
                        fieldWithPath("orderLineItems.[].orderId").type(NUMBER)
                                .description("order id of order line item"),
                        fieldWithPath("orderLineItems.[].quantity").type(NUMBER)
                                .description("quantity of order lien item"),
                        fieldWithPath("orderLineItems.[].orderedMenuId").type(NUMBER).description("menu id of order line item")
                )
        ));
    }
}
