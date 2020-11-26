package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class OrderRestControllerTest {
    private static final String API = "/api";
    private static final Long 메뉴_ID_1 = 1L;
    private static final Long 메뉴_ID_2 = 2L;
    private static final Long 메뉴_1개 = 1L;
    private static final Long 메뉴_2개 = 2L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Long 주문_ID_1 = 1L;
    private static final String 주문_상태_조리중 = "COOKING";
    private static final String 주문_상태_조리완료 = "COMPLETION";
    private static final LocalDateTime 주문_시간 = LocalDateTime.now();

    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("'/orders'로 POST 요청 시, 주문을 생성한다.")
    @Test
    void createTest() throws Exception {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(메뉴_ID_1, 메뉴_1개),
                new OrderLineItemRequest(메뉴_ID_2, 메뉴_2개)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(주문_ID_1, orderLineItems);
        OrderResponse orderResponse = new OrderResponse(주문_ID_1, null, 주문_상태_조리중, 주문_시간, null);
        when(orderService.create(any(OrderCreateRequest.class))).thenReturn(orderResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 생성 요청 시 잘못된 주문 ID를 보내면, 예외를 반환한다.")
    @Test
    void createWithNullOrderIdExceptionTest() throws Exception {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(메뉴_ID_1, 메뉴_1개),
                new OrderLineItemRequest(메뉴_ID_2, 메뉴_2개)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(null, orderLineItems);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 생성 요청 시 빈 메뉴 목록을 보내면, 예외를 반환한다.")
    @Test
    void createWithEmptyListExceptionTest() throws Exception {
        List<OrderLineItemRequest> orderLineItems = Collections.emptyList();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(주문_ID_1, orderLineItems);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 생성 요청 시 null인 메뉴 목록을 보내면, 예외를 반환한다.")
    @Test
    void createWithNullListExceptionTest() throws Exception {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(주문_ID_1, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 생성 요청 시 잘못된 메뉴 ID를 보내면, 예외를 반환한다.")
    @Test
    void createWithNullMenuIdExceptionTest() throws Exception {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(-1L, 메뉴_1개),
                new OrderLineItemRequest(메뉴_ID_2, 메뉴_2개)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(null, orderLineItems);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 생성 요청 시 null인 개수를 보내면, 예외를 반환한다.")
    @Test
    void createWithNullQuantityExceptionTest() throws Exception {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(메뉴_ID_1, -1L),
                new OrderLineItemRequest(메뉴_ID_2, 메뉴_2개)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(null, orderLineItems);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 생성 요청 시 잘못된 개수를 보내면, 예외를 반환한다.")
    @ValueSource(longs = {-1000, 0})
    @ParameterizedTest
    void createWithNullQuantityExceptionTest(Long invalidValue) throws Exception {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
                new OrderLineItemRequest(메뉴_ID_1, invalidValue),
                new OrderLineItemRequest(메뉴_ID_2, 메뉴_2개)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(null, orderLineItems);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderCreateRequest);

        this.mockMvc.perform(post(API + "/orders").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/orders'로 GET 요청 시, 주문의 목록을 반환한다.")
    @Test
    void listTest() throws Exception {
        OrderResponse orderResponse = new OrderResponse(주문_ID_1, null, 주문_상태_조리중, 주문_시간, null);
        List<OrderResponse> orderResponses = Arrays.asList(orderResponse);

        when(orderService.list()).thenReturn(orderResponses);

        this.mockMvc.perform(get(API + "/orders").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].id").value(주문_ID_1)).
                andExpect(jsonPath("$[0].orderStatus").value(주문_상태_조리중));
    }

    @DisplayName("'/orders/{orderId}/order-status'로 PUT 요청을 보내면, 주문의 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() throws Exception {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(주문_상태_조리완료);
        OrderResponse orderResponse = new OrderResponse(주문_ID_1, null, 주문_상태_조리완료, 주문_시간, null);
        when(orderService.changeOrderStatus(anyLong(), any(OrderStatusRequest.class))).thenReturn(orderResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderStatusRequest);

        this.mockMvc.perform(put(API + "/orders/" + 주문_ID_1 + "/order-status").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(주문_ID_1)).
                andExpect(jsonPath("$.orderStatus").value(주문_상태_조리완료)).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 상태 변경 중 유효하지 않은 요청을 보내면, 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void changeOrderStatusExceptionTest(String invalidValue) throws Exception {
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(invalidValue);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderStatusRequest);

        this.mockMvc.perform(put(API + "/orders/" + 주문_ID_1 + "/order-status").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }
}
