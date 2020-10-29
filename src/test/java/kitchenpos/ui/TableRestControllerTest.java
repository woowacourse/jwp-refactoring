package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    private static final String BASE_URL = "/api/tables";

    @MockBean
    private TableService tableService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("새로운 주문 테이블 생성")
    @Test
    void createTest() throws Exception {
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
        String content = objectMapper.writeValueAsString(orderTableRequest);
        OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L,
                orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());

        given(tableService.create(any())).willReturn(orderTableResponse);

        mockMvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.tableGroupId", Matchers.is(1)))
                .andExpect(jsonPath("$.numberOfGuests", Matchers.is(0)))
                .andExpect(jsonPath("$.empty", Matchers.is(true)));
    }

    @DisplayName("저장된 모든 주문 테이블 출력")
    @Test
    void listTest() throws Exception {
        List<OrderTableResponse> orderTableResponses = Arrays.asList(
                new OrderTableResponse(1L),
                new OrderTableResponse(2L),
                new OrderTableResponse(3L)
        );

        given(tableService.list()).willReturn(orderTableResponses);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(1)));
    }

    @DisplayName("주문 테이블에 주문이 비어있는 상태 여부 변경")
    @Test
    void changeEmptyTest() throws Exception {
        OrderTableRequest orderTableRequest = new OrderTableRequest(false);
        String content = objectMapper.writeValueAsString(orderTableRequest);
        OrderTableResponse orderTableResponse = new OrderTableResponse(1L);

        given(tableService.changeEmpty(any(), any())).willReturn(orderTableResponse);

        mockMvc.perform(
                put(BASE_URL + "/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }

    @DisplayName("주문 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4);
        String content = objectMapper.writeValueAsString(orderTableRequest);
        OrderTableResponse orderTableResponse = new OrderTableResponse(1L);

        given(tableService.changeNumberOfGuests(any(), any())).willReturn(orderTableResponse);

        mockMvc.perform(
                put(BASE_URL + "/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)));
    }
}
