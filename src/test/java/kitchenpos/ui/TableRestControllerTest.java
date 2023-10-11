package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 테이블_생성() throws Exception {
            // given
            given(tableService.create(any()))
                    .willReturn(orderTable);

            // when & then
            mockMvc.perform(post("/api/tables")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"numberOfGuests\": 0," +
                                    "\"empty\": true" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").exists(),
                            jsonPath("tableGroupId").value(orderTable.getTableGroupId()),
                            jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()),
                            jsonPath("empty").value(orderTable.isEmpty())
                    );
        }

        @Test
        void 테이블_목록_조회() throws Exception {
            // given
            given(tableService.list())
                    .willReturn(List.of(orderTable));

            // when & then
            mockMvc.perform(get("/api/tables"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$[0].id").value(orderTable.getId()),
                            jsonPath("$[0].tableGroupId").value(orderTable.getTableGroupId()),
                            jsonPath("$[0].numberOfGuests").value(orderTable.getNumberOfGuests()),
                            jsonPath("$[0].empty").value(orderTable.isEmpty())
                    );
        }

        @Test
        void 테이블_상태_변경() throws Exception {
            // given
            orderTable.setEmpty(false);
            given(tableService.changeEmpty(any(), any()))
                    .willReturn(orderTable);

            // when & then
            mockMvc.perform(put("/api/tables/1/empty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"empty\": false\n" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(orderTable.getId()),
                            jsonPath("tableGroupId").value(orderTable.getTableGroupId()),
                            jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()),
                            jsonPath("empty").value(orderTable.isEmpty())
                    );
        }

        @Test
        void 테이블_방문_손님_변경() throws Exception {
            // given
            orderTable.setNumberOfGuests(3);
            given(tableService.changeNumberOfGuests(any(), any()))
                    .willReturn(orderTable);

            // when & then
            mockMvc.perform(put("/api/tables/1/number-of-guests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"numberOfGuests\": 3\n" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(orderTable.getId()),
                            jsonPath("tableGroupId").value(orderTable.getTableGroupId()),
                            jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()),
                            jsonPath("empty").value(orderTable.isEmpty())
                    );
        }
    }
}
