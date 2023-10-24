package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        // given
        final OrderRequest orderRequest = new OrderRequest(
                1L,
                OrderStatus.COOKING.name(),
                List.of(new OrderLineItemRequest(1L, 1),
                        new OrderLineItemRequest(2L, 1)
                )
        );

        given(orderService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/orders/1"));
    }

    @DisplayName("입력받는 테이블 Id가 null이면 예외 처리한다.")
    @Test
    void create_FailWhenOrderTableIdNull() throws Exception {
        // given
        final OrderRequest orderRequest = new OrderRequest(
                null,
                OrderStatus.COOKING.name(),
                List.of(new OrderLineItemRequest(1L, 1),
                        new OrderLineItemRequest(2L, 1)
                )
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("입력받는 테이블의 상태가 null이면 예외 처리한다.")
    @Test
    void create_FailWhenOrderStatusNull() throws Exception {
        // given
        final OrderRequest orderRequest = new OrderRequest(
                1L,
                null,
                List.of(new OrderLineItemRequest(1L, 1),
                        new OrderLineItemRequest(2L, 1)
                )
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("입력받는 주문 항목 목록이 null이면 예외 처리한다.")
    @Test
    void create_FailWhenOrderLineItemsNull() throws Exception {
        // given
        final OrderRequest orderRequest = new OrderRequest(
                1L,
                OrderStatus.COOKING.name(),
                null
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("주문 목록을 반환할 수 있다.")
    @Test
    void list() throws Exception {
        // given
        final List<OrderResponse> orderResponses = List.of(
                OrderResponse.from(new Order(1L, OrderStatus.MEAL.name())),
                OrderResponse.from(new Order(2L, OrderStatus.COOKING.name()))
        );

        given(orderService.list())
                .willReturn(orderResponses);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/orders")
                .contentType(APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        final OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(OrderStatus.COOKING.name());
        final OrderResponse orderResponse = OrderResponse.from(new Order(2L, OrderStatus.COOKING.name()));

        given(orderService.changeOrderStatus(1L, updateRequest))
                .willReturn(orderResponse);

        // when
        final ResultActions resultActions = mockMvc.perform(put("/api/orders/{orderId}/order-status", 1)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("업데이트 할 테이블의 상태가 null이면 예외 처리한다.")
    @Test
    void changeOrderStatus_FailWhenStatusNull() throws Exception {
        // given
        final OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(null);

        // when
        final ResultActions resultActions = mockMvc.perform(put("/api/orders/{orderId}/order-status", 1)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
