package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuestsRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.RESPONSE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 테이블_생성() throws Exception {
            // given
            CreateOrderTableResponse response = RESPONSE.주문_테이블_생성_3명_응답();
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
            OrderTableResponse response = RESPONSE.주문_테이블_3명_응답();
            given(tableService.list())
                    .willReturn(List.of(response));

            // when & then
            mockMvc.perform(get("/api/tables"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$[0].id").value(response.getId()),
                            jsonPath("$[0].tableGroupId").value(response.getTableGroupId()),
                            jsonPath("$[0].numberOfGuests").value(response.getNumberOfGuests()),
                            jsonPath("$[0].empty").value(response.isEmpty())
                    );
        }

        @Test
        void 테이블_상태_변경() throws Exception {
            // given
            OrderTableResponse response = RESPONSE.주문_테이블_3명_응답();
            given(tableService.changeEmpty(anyLong(), any(UpdateOrderTableEmptyRequest.class)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(put("/api/tables/1/empty")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    " \"empty\": false" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(response.getId()),
                            jsonPath("tableGroupId").value(response.getTableGroupId()),
                            jsonPath("numberOfGuests").value(response.getNumberOfGuests()),
                            jsonPath("empty").value(response.isEmpty())
                    );
        }

        @Test
        void 테이블_방문_손님_변경() throws Exception {
            // given
            OrderTableResponse response = RESPONSE.주문_테이블_3명_응답();
            given(tableService.changeNumberOfGuests(anyLong(), any(UpdateOrderTableGuestsRequest.class)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(put("/api/tables/1/number-of-guests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"numberOfGuests\": 3" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("id").value(response.getId()),
                            jsonPath("tableGroupId").value(response.getTableGroupId()),
                            jsonPath("numberOfGuests").value(response.getNumberOfGuests()),
                            jsonPath("empty").value(response.isEmpty())
                    );
        }
    }
}
