package kitchenpos.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.ordertable.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TableGroupService tableGroupService;

    @Test
    @DisplayName("POST /api/table-groups")
    void createProduct() throws Exception {

        final OrderTable orderTable = new OrderTable(1L, 1L, new NumberOfGuests(6), false);
        final OrderTable orderTable2 = new OrderTable(2L, 1L, new NumberOfGuests(9), false);
        final TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(orderTable, orderTable2));

        final var request = Map.of(
                "orderTables", List.of(
                        Map.of("id", 1),
                        Map.of("id", 2)
                )
        );

        when(tableGroupService.create(List.of(1L, 2L))).thenReturn(TableGroupResponse.from(tableGroup));
        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
