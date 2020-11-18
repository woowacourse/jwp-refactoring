package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import kitchenpos.dto.order.TableGroupCreateRequest;
import kitchenpos.dto.order.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TableGroupRestControllerTest {
    private static final String API = "/api";
    private static final Long 테이블_ID_1 = 1L;
    private static final Long 테이블_ID_2 = 2L;
    private static final Integer 테이블_사람_1명 = 1;
    private static final Boolean 테이블_비어있음 = true;
    private static final List<OrderTableResponse> 테이블_반환_목록 = Arrays.asList(
            new OrderTableResponse(테이블_ID_1, 테이블_사람_1명, 테이블_비어있음),
            new OrderTableResponse(테이블_ID_2, 테이블_사람_1명, 테이블_비어있음)
    );
    private static final Long 테이블_그룹_ID_1 = 1L;
    private static final LocalDateTime 테이블_그룹_생성시간 = LocalDateTime.now();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private TableGroupService tableGroupService;

    private MockMvc mockMvc;
    private List<OrderTableRequest> orderTableRequests;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        orderTableRequests = Arrays.asList(
                new OrderTableRequest(1L),
                new OrderTableRequest(2L)
        );
    }

    @DisplayName("'/table-groups'로 POST 요청 시, 테이블 그룹을 생성한다")
    @Test
    void createTest() throws Exception {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableRequests);
        TableGroupResponse tableGroupResponse = new TableGroupResponse(테이블_그룹_ID_1, 테이블_그룹_생성시간, 테이블_반환_목록);
        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenReturn(tableGroupResponse);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(tableGroupCreateRequest);

        this.mockMvc.perform(post(API + "/table-groups").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }
    // TODO: 2020/11/17 예외 테스트 : 리퀘스트가 빈 경우와 안에 값으로 Null 값!

    @DisplayName("예외 테스트: 만약 TableGroup 생성 요청이 비어 있으면, 예외를 반환한다.")
    @Test
    void createWithCreateRequestEmptyExceptionTest() throws Exception {
        TableGroupCreateRequest invalidRequest = new TableGroupCreateRequest(Collections.emptyList());
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/table-groups").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("예외 테스트: 만약 TableGroup 생성 요청이 유효하지 않으면, 예외를 반환한다.")
    @Test
    void createWithCreateRequestInvalidExceptionTest() throws Exception {
        List<OrderTableRequest> invalidOrderTableRequests = Arrays.asList(
                new OrderTableRequest(null)
        );
        TableGroupCreateRequest invalidRequest = new TableGroupCreateRequest(invalidOrderTableRequests);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(invalidRequest);

        this.mockMvc.perform(post(API + "/table-groups").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    Objects.requireNonNull(exception);
                    assertTrue(exception.getClass().isAssignableFrom(MethodArgumentNotValidException.class));
                }).
                andDo(print());
    }

    @DisplayName("'/table-groups'로 DELETE 요청 시, 테이블 그룹을 삭제한다.")
    @Test
    void ungroupTest() throws Exception {
        doNothing().when(tableGroupService).ungroup(anyLong());

        this.mockMvc.perform(delete(API + "/table-groups/" + 테이블_그룹_ID_1).
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(status().isNoContent()).
                andDo(print());
    }
}
