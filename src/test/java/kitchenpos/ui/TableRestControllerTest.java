package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.KitchenPosTestFixture;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = TableRestController.class)
@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends KitchenPosTestFixture {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    private OrderTable firstTable;
    private OrderTable secondTable;

    @BeforeEach
    void setUp() {
        firstTable = 주문_테이블을_저장한다(1L, 1L, 3, false);
        secondTable = 주문_테이블을_저장한다(2L, 1L, 3, true);
    }

    @Test
    void create() throws Exception {
        // given
        // when
        given(tableService.create(any(OrderTable.class))).willReturn(firstTable);

        // then
        mvc.perform(post("/api/tables")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstTable)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(firstTable.getId().intValue())))
                .andExpect(jsonPath("$.tableGroupId", is(firstTable.getTableGroupId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(firstTable.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(firstTable.isEmpty())));
    }

    @Test
    void list() throws Exception {
        // given
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        // when
        given(tableService.list()).willReturn(orderTables);

        // then
        mvc.perform(MockMvcRequestBuilders.get("/api/tables")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstTable.getId().intValue())))
                .andExpect(jsonPath("$[1].id", is(secondTable.getId().intValue())));
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        // when
        secondTable.setEmpty(true);
        given(tableService.changeEmpty(any(Long.class), any(OrderTable.class))).willReturn(firstTable);

        // then
        mvc.perform(put("/api/tables/{orderTableId}/empty", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstTable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstTable.getId().intValue())))
                .andExpect(jsonPath("$.tableGroupId", is(firstTable.getTableGroupId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(firstTable.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(firstTable.isEmpty())));
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        // when
        firstTable.setNumberOfGuests(7);
        given(tableService.changeNumberOfGuests(any(Long.class), any(OrderTable.class))).willReturn(firstTable);

        // then
        mvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstTable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstTable.getId().intValue())))
                .andExpect(jsonPath("$.tableGroupId", is(firstTable.getTableGroupId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(firstTable.getNumberOfGuests())))
                .andExpect(jsonPath("$.empty", is(firstTable.isEmpty())));
    }
}