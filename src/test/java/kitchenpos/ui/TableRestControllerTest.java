package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.TableService;
import kitchenpos.domain.Table;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestsRequest;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends MvcTest {

    @MockBean
    private TableService tableService;

    @DisplayName("/api/tables로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(tableService.create()).willReturn(TABLE_1);

        MvcResult mvcResult = postAction("/api/tables")
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/tables/1"))
            .andReturn();

        Table tableResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Table.class);
        assertThat(tableResponse).usingRecursiveComparison().isEqualTo(TABLE_1);
    }

    @DisplayName("/api/tables로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(tableService.list()).willReturn(Arrays.asList(TABLE_1, TABLE_2));

        MvcResult mvcResult = getAction("/api/tables")
            .andExpect(status().isOk())
            .andReturn();

        List<Table> tablesResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Table>>() {});
        assertAll(
            () -> assertThat(tablesResponse.size()).isEqualTo(2),
            () -> assertThat(tablesResponse.get(0)).usingRecursiveComparison().isEqualTo(TABLE_1),
            () -> assertThat(tablesResponse.get(1)).usingRecursiveComparison().isEqualTo(TABLE_2)
        );
    }

    @DisplayName("/api/tables/{tableId}/empty로 PUT 요청 성공 테스트")
    @Test
    void changeEmptyTest() throws Exception {
        given(tableService.changeEmpty(anyLong(), anyBoolean())).willReturn(TABLE_1);

        String inputJson = objectMapper.writeValueAsString(new ChangeEmptyRequest(TABLE_EMPTY_1));
        MvcResult mvcResult = putAction(String.format("/api/tables/%d/empty", TABLE_ID_1), inputJson)
            .andExpect(status().isOk())
            .andReturn();

        Table tableResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Table.class);
        assertThat(tableResponse).usingRecursiveComparison().isEqualTo(TABLE_1);
    }

    @DisplayName("api/table/{tableId}/number-of-guests로 PUT 요청 성공 테스트")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        given(tableService.changeNumberOfGuests(anyLong(), anyInt())).willReturn(TABLE_1);

        String inputJson = objectMapper.writeValueAsString(new ChangeNumberOfGuestsRequest(TABLE_NUMBER_OF_GUESTS_1));
        MvcResult mvcResult = putAction(String.format("/api/tables/%d/number-of-guests", TABLE_ID_1), inputJson)
            .andExpect(status().isOk())
            .andReturn();

        Table tableResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Table.class);
        assertThat(tableResponse).usingRecursiveComparison().isEqualTo(TABLE_1);
    }
}