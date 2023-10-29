package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.service.TableService;
import kitchenpos.util.ObjectCreator;
import kitchenpos.value.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TableRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final CreateOrderTableRequest request = ObjectCreator.getObject(CreateOrderTableRequest.class, 1);
        final OrderTable orderTable = ObjectCreator.getObject(OrderTable.class, 1L, null, new NumberOfGuests(1), false);

        when(tableService.create(any()))
                .thenReturn(OrderTableResponse.from(orderTable));

        // when & then
        mockMvc.perform(post("/api/tables")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("tableGroupId").isEmpty())
                .andExpect(jsonPath("numberOfGuests").isNumber())
                .andExpect(jsonPath("empty").isBoolean());
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        when(tableService.list())
                .thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @DisplayName("테이블 상태 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        // given
        final ChangeEmptyRequest request = ObjectCreator.getObject(ChangeEmptyRequest.class, true);
        final OrderTable orderTable = ObjectCreator.getObject(OrderTable.class, 1L, null, new NumberOfGuests(1), true);

        when(tableService.changeEmpty(any(), any()))
                .thenReturn(OrderTableResponse.from(orderTable));

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("numberOfGuests").isNumber())
                .andExpect(jsonPath("empty").isBoolean());
    }

    @DisplayName("테이블 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        final ChangeNumberOfGuestsRequest request = ObjectCreator.getObject(ChangeNumberOfGuestsRequest.class, 2);
        final OrderTable orderTable = ObjectCreator.getObject(OrderTable.class, 1L, null, new NumberOfGuests(2), true);

        when(tableService.changeNumberOfGuests(any(), any()))
                .thenReturn(OrderTableResponse.from(orderTable));

        // when & then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("numberOfGuests").isNumber())
                .andExpect(jsonPath("empty").isBoolean());
    }
}
