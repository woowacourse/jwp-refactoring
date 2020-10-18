package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.dto.table.OrderTableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블 생성 요청 요청 테스트")
    @Test
    void create() throws Exception {
        OrderTableResponse response =
                new OrderTableResponse(1L, null, 0, true);

        given(tableService.create(any())).willReturn(response);

        mockMvc.perform(post("/api/tables")
                .content("{\n"
                        + "  \"numberOfGuests\": 0,\n"
                        + "  \"empty\": true\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/1"))
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.tableGroupId", Matchers.nullValue()))
                .andExpect(jsonPath("$.numberOfGuests", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.empty", Matchers.instanceOf(Boolean.class)));
    }

    @DisplayName("테이블 목록 요청 테스트")
    @Test
    void list() throws Exception {
        List<OrderTableResponse> orderTables = new ArrayList<>();
        orderTables.add(new OrderTableResponse());
        orderTables.add(new OrderTableResponse());

        given(tableService.list()).willReturn(orderTables);

        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @DisplayName("테이블의 setEmpty를 변경 요청 테스트")
    @Test
    void changeEmpty() throws Exception {
        OrderTableResponse orderTableResponse = new OrderTableResponse(1L, null, 1, false);

        given(tableService.changeEmpty(anyLong(), any())).willReturn(orderTableResponse);

        mockMvc.perform(put("/api/tables/1/empty")
                .content("{\n"
                        + "  \"empty\": false\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.empty", Matchers.is(false)));
    }

    @DisplayName("고객 수 변경 요청 테스트")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTableResponse orderTable = new OrderTableResponse(1L, null, 4, false);

        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(orderTable);


        mockMvc.perform(put("/api/tables/1/number-of-guests")
                .content("{\n"
                        + "  \"numberOfGuests\": 4\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests", Matchers.is(4)));

    }
}
