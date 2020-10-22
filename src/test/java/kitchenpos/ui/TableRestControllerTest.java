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
import kitchenpos.domain.OrderTable;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends MvcTest {

    @MockBean
    private TableService tableService;

    @DisplayName("/api/tables로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(tableService.create(any())).willReturn(ORDER_TABLE_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_TABLE_1);
        MvcResult mvcResult = postAction("/api/tables", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/tables/1"))
            .andReturn();

        OrderTable orderTableResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderTable.class);
        assertThat(orderTableResponse).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }

    @DisplayName("/api/tables로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(tableService.list()).willReturn(Arrays.asList(ORDER_TABLE_1, ORDER_TABLE_2));

        MvcResult mvcResult = getAction("/api/tables")
            .andExpect(status().isOk())
            .andReturn();

        List<OrderTable> orderTablesResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<OrderTable>>() {});
        assertAll(
            () -> assertThat(orderTablesResponse.size()).isEqualTo(2),
            () -> assertThat(orderTablesResponse.get(0)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1),
            () -> assertThat(orderTablesResponse.get(1)).usingRecursiveComparison().isEqualTo(ORDER_TABLE_2)
        );
    }

    @DisplayName("/api/tables/{orderTableId}/empty로 PUT 요청 성공 테스트")
    @Test
    void changeEmptyTest() throws Exception {
        given(tableService.changeEmpty(anyLong(), any())).willReturn(ORDER_TABLE_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_TABLE_1);
        MvcResult mvcResult = putAction(String.format("/api/tables/%d/empty", ORDER_TABLE_ID_1), inputJson)
            .andExpect(status().isOk())
            .andReturn();

        OrderTable orderTableResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderTable.class);
        assertThat(orderTableResponse).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }

    @DisplayName("api/table/{orderTableId}/number-of-guests로 PUT 요청 성공 테스트")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        given(tableService.changeNumberOfGuests(anyLong(), any())).willReturn(ORDER_TABLE_1);

        String inputJson = objectMapper.writeValueAsString(ORDER_TABLE_1);
        MvcResult mvcResult = putAction(String.format("/api/tables/%d/number-of-guests", ORDER_TABLE_ID_1), inputJson)
            .andExpect(status().isOk())
            .andReturn();

        OrderTable orderTableResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderTable.class);
        assertThat(orderTableResponse).usingRecursiveComparison().isEqualTo(ORDER_TABLE_1);
    }
}