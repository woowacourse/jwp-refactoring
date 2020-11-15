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

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(null);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(null);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);

        OrderTable orderTable3 = new OrderTable();
        orderTable3.setId(3L);
        orderTable3.setTableGroupId(null);
        orderTable3.setNumberOfGuests(0);
        orderTable3.setEmpty(true);

        tables = Arrays.asList(orderTable1, orderTable2, orderTable3);
    }

    @DisplayName("테이블을 정상적으로 생성한다.")
    @Test
    void createTable() throws Exception {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(null);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        when(tableService.create(any(OrderTable.class))).thenReturn(tables.get(0));

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
        when(tableService.list()).thenReturn(tables);

        mockMvc.perform(get("/api/tables")
            .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").value(1L))
            .andExpect(jsonPath("[1].id").value(2L))
            .andExpect(jsonPath("[2].id").value(3L));
    }

    @DisplayName("Empty상태를 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        OrderTable table = tables.get(0);
        table.setEmpty(false);
        when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(table);

        mockMvc.perform(put("/api/tables/{id}/empty", table.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(table))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("empty").value(false));
    }

    @DisplayName("손님의 수를 수정한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable table = tables.get(0);
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