package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.fixture.OrderFixture;
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

        when(tableService.create(any(OrderTableCreateRequest.class))).thenReturn(orderTable);

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
        when(tableService.list()).thenReturn(Arrays.asList(table1, table2));

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
        when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(response);

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
        OrderTable table = OrderTableFixture.createNotEmptyWithId(1L);
        table.setNumberOfGuests(18);
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).thenReturn(table);

        mockMvc.perform(put("/api/tables/{id}/number-of-guests", table.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(table))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("numberOfGuests").value(18));
    }
}
