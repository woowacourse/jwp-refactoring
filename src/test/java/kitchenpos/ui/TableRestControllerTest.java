package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
public class TableRestControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TableService tableService;

    @Test
    @DisplayName("POST /api/tables - Table 생성")
    public void create() throws Exception {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(true);
        given(tableService.create(any(OrderTable.class))).willReturn(orderTable);

        //when & then
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("tableGroupId").value(1L))
                .andExpect(jsonPath("numberOfGuests").value(2))
                .andExpect(jsonPath("empty").value(true));
    }

    @Test
    @DisplayName("GET /api/tables - Table 목록 조회")
    public void list() throws Exception {
        //given
        final List<OrderTable> orderTables = new ArrayList<>();
        final OrderTable orderTable1 = new OrderTable();
        orderTables.add(orderTable1);
        orderTable1.setTableGroupId(1L);
        orderTable1.setNumberOfGuests(2);
        orderTable1.setEmpty(false);

        final OrderTable orderTable2 = new OrderTable();
        orderTables.add(orderTable2);
        orderTable2.setTableGroupId(1L);
        orderTable2.setNumberOfGuests(4);
        orderTable2.setEmpty(false);
        given(tableService.list()).willReturn(orderTables);

        //when & then
        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tableGroupId").value(1L))
                .andExpect(jsonPath("$[0].numberOfGuests").value(2))
                .andExpect(jsonPath("$[0].empty").value(false))
                .andExpect(jsonPath("$[1].tableGroupId").value(1L))
                .andExpect(jsonPath("$[1].numberOfGuests").value(4))
                .andExpect(jsonPath("$[1].empty").value(false));
    }

    @Test
    @DisplayName("PUT /api/tables/{orderTableId}/empty - Table Empty 상태 변경")
    public void changeEmpty() throws Exception {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(tableService.changeEmpty(anyLong(), any(OrderTable.class))).willReturn(orderTable);

        //when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("empty").value(true));
    }

    @Test
    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests - Table 인원 수 변경")
    public void changeNumberOfGuests() throws Exception {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        given(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).willReturn(orderTable);

        //when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("numberOfGuests").value(4));
    }
}
