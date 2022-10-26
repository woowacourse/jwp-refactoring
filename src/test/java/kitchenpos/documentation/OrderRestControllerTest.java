package kitchenpos.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class OrderRestControllerTest extends DocumentationTest {
    private static final String ORDER_API_URL = "/api/orders";

    @DisplayName("POST " + ORDER_API_URL)
    @Test
    void create() {
        given(orderService.create(any()))
                .willReturn(
                        new Order(1L, 5L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                List.of(
                                        new OrderLineItem(1L, 1L, 1L, 1L),
                                        new OrderLineItem(2L, 1L, 3L, 2L)
                                )
                        )
                );
        final var orderCreateRequest = new OrderCreateRequest(5L, List.of(
                new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(3L, 2L)
        ));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderCreateRequest)
                .when().post(ORDER_API_URL)
                .then().log().all()
                .apply(document("orders/create",
                        requestFields(
                                fieldWithPath("orderTableId").type(JsonFieldType.NUMBER).description("주문 테이블 아이디"),
                                fieldWithPath("orderLineItems.[].menuId").type(JsonFieldType.NUMBER)
                                        .description("메뉴 아이디"),
                                fieldWithPath("orderLineItems.[].quantity").type(JsonFieldType.NUMBER)
                                        .description("메뉴 수량")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 아이디"),
                                fieldWithPath("orderTableId").type(JsonFieldType.NUMBER).description("주문 테이블 아이디"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                                fieldWithPath("orderedTime").type(JsonFieldType.STRING).description("주문 일시"),
                                fieldWithPath("orderLineItems.[].seq").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 seq"),
                                fieldWithPath("orderLineItems.[].orderId").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 주문 아이디"),
                                fieldWithPath("orderLineItems.[].menuId").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 메뉴 아이디"),
                                fieldWithPath("orderLineItems.[].quantity").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 메뉴 수량")
                        )
                ))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("GET " + ORDER_API_URL)
    @Test
    void list() {
        given(orderService.list())
                .willReturn(
                        List.of(
                                new Order(1L, 5L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                        List.of(
                                                new OrderLineItem(1L, 1L, 1L, 1L),
                                                new OrderLineItem(2L, 1L, 3L, 2L)
                                        )
                                ),
                                new Order(2L, 7L, OrderStatus.COOKING.name(), LocalDateTime.now().plusMinutes(1L),
                                        List.of(
                                                new OrderLineItem(3L, 2L, 11L, 2L),
                                                new OrderLineItem(4L, 2L, 23L, 1L)
                                        )
                                )
                        )
                );

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(ORDER_API_URL)
                .then().log().all()
                .apply(document("orders/list",
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("주문 아이디"),
                                fieldWithPath("[].orderTableId").type(JsonFieldType.NUMBER).description("주문 테이블 아이디"),
                                fieldWithPath("[].orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                                fieldWithPath("[].orderedTime").type(JsonFieldType.STRING).description("주문 일시"),
                                fieldWithPath("[].orderLineItems.[].seq").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 seq"),
                                fieldWithPath("[].orderLineItems.[].orderId").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 주문 아이디"),
                                fieldWithPath("[].orderLineItems.[].menuId").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 메뉴 아이디"),
                                fieldWithPath("[].orderLineItems.[].quantity").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 메뉴 수량")
                        )
                ))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("PUT " + ORDER_API_URL)
    @Test
    void changeOrderStatus() {
        given(orderService.changeOrderStatus(any(), any()))
                .willReturn(
                        new Order(1L, 5L, OrderStatus.COMPLETION.name(), LocalDateTime.now().minusHours(1L),
                                List.of(
                                        new OrderLineItem(1L, 1L, 1L, 1L),
                                        new OrderLineItem(2L, 1L, 3L, 2L)
                                )
                        )
                );
        final var orderChangeStatusRequest = new OrderChangeStatusRequest(OrderStatus.COMPLETION.name());

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderChangeStatusRequest)
                .when().put("/api/orders/{orderId}/order-status", 1)
                .then().log().all()
                .apply(document("orders/changeOrderStatus",
                        pathParameters(parameterWithName("orderId").description("수정 대상 주문 아이디")),
                        requestFields(
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("수정 희망 주문 상태")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주문 아이디"),
                                fieldWithPath("orderTableId").type(JsonFieldType.NUMBER).description("주문 테이블 아이디"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("주문 상태"),
                                fieldWithPath("orderedTime").type(JsonFieldType.STRING).description("주문 일시"),
                                fieldWithPath("orderLineItems.[].seq").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 seq"),
                                fieldWithPath("orderLineItems.[].orderId").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 주문 아이디"),
                                fieldWithPath("orderLineItems.[].menuId").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 메뉴 아이디"),
                                fieldWithPath("orderLineItems.[].quantity").type(JsonFieldType.NUMBER)
                                        .description("주문 아이템 메뉴 수량")
                        )
                ))
                .statusCode(HttpStatus.OK.value());
    }
}
