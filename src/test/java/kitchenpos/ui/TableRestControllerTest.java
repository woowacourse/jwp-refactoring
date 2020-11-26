package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.dto.order.OrderTableChangeEmptyRequest;
import kitchenpos.dto.order.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.order.OrderTableCreateRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
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
public class TableRestControllerTest {
    private static final String API = "/api";
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;
    private static final Boolean 테이블_비어있지않음 = false;
    private static final Long 테이블_ID_1 = 1L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private TableService tableService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("'/tables'로 POST 요청 시, 테이블을 생성한다")
    @Test
    void createTest() throws Exception {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(테이블_사람_1명, 테이블_비어있음);
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음);
        when(tableService.create(any(OrderTableCreateRequest.class))).thenReturn(orderTableResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderTableCreateRequest);

        this.mockMvc.perform(post(API + "/tables").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 OrderTable 생성 요청의 사람 수가 0 이하이면, 예외를 반환한다.")
    @ValueSource(ints = {-1000, 0})
    @ParameterizedTest
    void createWithNegativeNumberOfGuestExceptionTest(int invalidNumberOfGuest) throws Exception {
        OrderTableCreateRequest invalidRequest = new OrderTableCreateRequest(invalidNumberOfGuest, 테이블_비어있음);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/tables").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 OrderTable 생성 요청의 사람 수가 Null이면, 예외를 반환한다.")
    @Test
    void createWithNullNumberOfGuestExceptionTest() throws Exception {
        OrderTableCreateRequest invalidRequest = new OrderTableCreateRequest(null, 테이블_비어있음);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/tables").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 OrderTable 생성 요청의 사람 수가 Null이면, 예외를 반환한다.")
    @Test
    void createWithNullEmptyExceptionTest() throws Exception {
        OrderTableCreateRequest invalidRequest = new OrderTableCreateRequest(테이블_사람_1명, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/tables").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/tables'로 GET 요청 시, 테이블의 목록을 반환한다.")
    @Test
    void listTest() throws Exception {
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음);
        List<OrderTableResponse> orderTableResponses = Arrays.asList(orderTableResponse);

        when(tableService.list()).thenReturn(orderTableResponses);

        this.mockMvc.perform(get(API + "/tables").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].id").value(테이블_ID_1)).
                andExpect(jsonPath("$[0].numberOfGuests").value(테이블_사람_1명)).
                andExpect(jsonPath("$[0].empty").value(테이블_비어있음));
    }

    @DisplayName("'/tables/{orderTableId}/empty'로 PUT 요청을 보내면, 테이블의 비어있음 여부를 변경한다.")
    @Test
    void changeEmptyTest() throws Exception {
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(테이블_비어있지않음);
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음);

        when(tableService.changeEmpty(anyLong(), any(OrderTableChangeEmptyRequest.class))).thenReturn(orderTableResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderTableChangeEmptyRequest);

        this.mockMvc.perform(put(API + "/tables/" + 테이블_ID_1 + "/empty").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(테이블_ID_1)).
                andExpect(jsonPath("$.numberOfGuests").value(테이블_사람_1명)).
                andExpect(jsonPath("$.empty").value(테이블_비어있지않음));
    }

    @DisplayName("예외 테스트: 만약 테이블의 비어있음 여부를 변경하는 요청의 empty 값이 Null이면, 예외를 발생시킨다.")
    @Test
    void changeEmptyWithNullExceptionTest() throws Exception {
        OrderTableChangeEmptyRequest invalidRequest = new OrderTableChangeEmptyRequest(null);
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_1명, 테이블_비어있지않음);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(put(API + "/tables/" + 테이블_ID_1 + "/empty").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/tables/{orderTableId}/number-of-guests'로 PUT 요청을 보내면, 테이블의 인원 수를 변경한다.")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(테이블_사람_2명);
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_2명, 테이블_비어있음);
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTableChangeNumberOfGuestsRequest.class))).thenReturn(orderTableResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(orderTableChangeNumberOfGuestsRequest);

        this.mockMvc.perform(put(API + "/tables/" + 테이블_ID_1 + "/number-of-guests").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(테이블_ID_1)).
                andExpect(jsonPath("$.numberOfGuests").value(테이블_사람_2명)).
                andExpect(jsonPath("$.empty").value(테이블_비어있음));
    }

    @DisplayName("예외 테스트: 만약 테이블의 인원을 변경하는 요청의 인원 값이 Null이면, 예외를 발생시킨다.")
    @Test
    void changeNumberOfGuestsWithNullExceptionTest() throws Exception {
        OrderTableChangeNumberOfGuestsRequest invalidRequest = new OrderTableChangeNumberOfGuestsRequest(null);
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_2명, 테이블_비어있음);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(put(API + "/tables/" + 테이블_ID_1 + "/number-of-guests").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 테이블의 인원을 변경하는 요청의 인원 값이 0 이하면, 예외를 발생시킨다.")
    @ValueSource(ints = {-1000, 0})
    @ParameterizedTest
    void changeNumberOfGuestsWithNegativeExceptionTest(int invalidNumberOfGuests) throws Exception {
        OrderTableChangeNumberOfGuestsRequest invalidRequest = new OrderTableChangeNumberOfGuestsRequest(invalidNumberOfGuests);
        OrderTableResponse orderTableResponse = new OrderTableResponse(테이블_ID_1, 테이블_사람_2명, 테이블_비어있음);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(put(API + "/tables/" + 테이블_ID_1 + "/number-of-guests").
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
