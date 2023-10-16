package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        orderTable = OrderTable.builder()
                .id(1L)
                .tableGroupId(1L)
                .numberOfGuests(0)
                .empty(true)
                .build();
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 테이블_생성() throws Exception {
            // given
            CreateOrderTableResponse response = OrderTableFixture.RESPONSE.주문_테이블_생성_3명_응답();
            given(tableService.create(any(CreateOrderTableRequest.class)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/tables")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"numberOfGuests\": 3," +
                                    "\"empty\": true" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").exists(),
                            jsonPath("tableGroupId").value(response.getTableGroupId()),
                            jsonPath("numberOfGuests").value(response.getNumberOfGuests()),
                            jsonPath("empty").value(response.isEmpty())
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
            OrderTable updated = orderTable.updateEmpty(false);
            given(tableService.changeEmpty(any(), any()))
                    .willReturn(updated);

            // when & then
            mockMvc.perform(put("/api/tables/1/empty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"empty\": false\n" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(updated.getId()),
                            jsonPath("tableGroupId").value(updated.getTableGroupId()),
                            jsonPath("numberOfGuests").value(updated.getNumberOfGuests()),
                            jsonPath("empty").value(updated.isEmpty())
                    );
        }

        @Test
        void 테이블_방문_손님_변경() throws Exception {
            // given
            OrderTable updated = orderTable.updateNumberOfGuests(3);
            given(tableService.changeNumberOfGuests(any(), any()))
                    .willReturn(updated);

            // when & then
            mockMvc.perform(put("/api/tables/1/number-of-guests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"numberOfGuests\": 3\n" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(updated.getId()),
                            jsonPath("tableGroupId").value(updated.getTableGroupId()),
                            jsonPath("numberOfGuests").value(updated.getNumberOfGuests()),
                            jsonPath("empty").value(updated.isEmpty())
                    );
        }
    }
}
