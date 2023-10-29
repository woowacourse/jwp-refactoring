package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.TableCreateRequest;
import kitchenpos.table.dto.request.TableUpdateEmptyRequest;
import kitchenpos.table.dto.request.TableUpdateGuestRequest;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.table.ui.TableRestController;

@WebMvcTest(TableRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @DisplayName("테이블을 올바르게 생성할 수 있다.")
    @Test
    void create() throws Exception {
        // given
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(2, true);

        given(tableService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/tables")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableCreateRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/tables/1"));
    }

    @DisplayName("인원 수가 올바르지 않으면 예외 처리한다.")
    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    void create_failWhenNumberOfGuestInvalid(final Integer numberOfGuests) throws Exception {
        // given
        final TableCreateRequest tableCreateRequest = new TableCreateRequest(numberOfGuests, true);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/tables")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("테이블 목록을 반환할 수 있다.")
    @Test
    void list() throws Exception {
        // given
        final List<TableResponse> tableResponses = List.of(
                TableResponse.from(new OrderTable(0, true)),
                TableResponse.from(new OrderTable(2, false))
        );

        given(tableService.list())
                .willReturn(tableResponses);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/tables")
                .contentType(APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("테이블의 빈 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() throws Exception{
        // given
        final TableUpdateEmptyRequest updateRequest = new TableUpdateEmptyRequest(false);
        final TableResponse tableResponse = TableResponse.from(new OrderTable(2, false));

        given(tableService.changeEmpty(anyLong(), any()))
                .willReturn(tableResponse);

        // when
        final ResultActions resultActions = mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @DisplayName("테이블에 할당된 인원 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() throws Exception{
        // given
        final TableUpdateGuestRequest updateRequest = new TableUpdateGuestRequest(0);
        final TableResponse tableResponse = TableResponse.from(new OrderTable(2, false));

        given(tableService.changeNumberOfGuests(anyLong(), any()))
                .willReturn(tableResponse);

        // when
        final ResultActions resultActions = mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        resultActions.andExpect(status().isOk());
    }
}
