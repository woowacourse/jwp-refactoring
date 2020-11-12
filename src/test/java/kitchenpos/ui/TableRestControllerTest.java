package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.fixture.OrderTableFixture;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 정상적으로 생성한다.")
    @Test
    void createTable() throws Exception {
        OrderTable orderTable = OrderTableFixture.createEmptyWithId(1L);

        when(tableService.create(any(OrderTableRequest.class))).thenReturn(orderTable);

        mockMvc.perform(post("/api/tables")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(orderTable))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(1L))
            .andExpect(jsonPath("tableGroupId").isEmpty())
            .andExpect(jsonPath("numberOfGuests").value(0))
            .andExpect(jsonPath("empty").value(true));
    }

    @DisplayName("테이블을 모두 조회한다.")
    @Test
    void findAll() throws Exception {
        OrderTable table1 = OrderTableFixture.createEmptyWithId(1L);
        OrderTable table2 = OrderTableFixture.createEmptyWithId(2L);
        when(tableService.list()).thenReturn(
            OrderTableResponse.listOf(Arrays.asList(table1, table2)));

        mockMvc.perform(get("/api/tables")
            .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").value(1L))
            .andExpect(jsonPath("[1].id").value(2L));
    }

    @DisplayName("Empty상태를 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTableResponse response = OrderTableFixture.createResponse(1L);
        when(tableService.changeEmpty(anyLong(), any(OrderTableRequest.class))).thenReturn(
            response);

        mockMvc.perform(put("/api/tables/{id}/empty", response.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(response))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("empty").value(false));
    }

    @DisplayName("손님의 수를 수정한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTableResponse response = OrderTableResponse.of(OrderTableFixture.createNumOf(1L, 18));
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTableRequest.class))).thenReturn(
            response);

        mockMvc.perform(put("/api/tables/{id}/number-of-guests", response.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(response))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("numberOfGuests").value(18));
    }
}
