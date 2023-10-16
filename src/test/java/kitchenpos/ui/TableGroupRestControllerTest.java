package kitchenpos.ui;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = OrderTable.builder()
                .id(1L)
                .tableGroupId(1L)
                .numberOfGuests(0)
                .empty(true)
                .build();

        tableGroup = TableGroup.builder()
                .id(1L)
                .createdDate(LocalDateTime.of(2023, Month.APRIL, 1, 0, 0, 0, 0))
                .orderTables(List.of(orderTable))
                .build();
    }


    @Nested
    class 정상_요청_테스트 {

        @Test
        void 주문_테이블_그룹_생성() throws Exception {
            // given
            CreateTableGroupResponse response = TableGroupFixture.RESPONSE.주문_테이블_그룹_생성_응답();
            given(tableGroupService.create(any(CreateTableGroupRequest.class)))
                    .willReturn(response);

            // when & then
            mockMvc.perform(post("/api/table-groups")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"orderTables\":[" +
                                    "{" +
                                    "\"id\":1," +
                                    "\"tableGroupId\":1," +
                                    "\"numberOfGuests\":0," +
                                    "\"empty\":true" +
                                    "}" +
                                    "]" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").value(tableGroup.getId()),
                            jsonPath("orderTables").isArray(),
                            jsonPath("orderTables[0].id").value(response.getId()),
                            jsonPath("orderTables[0].tableGroupId").value(response.getOrderTables().get(0).getTableGroupId()),
                            jsonPath("orderTables[0].numberOfGuests").value(response.getOrderTables().get(0).getNumberOfGuests()),
                            jsonPath("orderTables[0].empty").value(response.getOrderTables().get(0).isEmpty())
                    );
        }

        @Test
        void 주문_테이블_그룹_삭제() throws Exception {
            // given
            willDoNothing()
                    .given(tableGroupService)
                    .ungroup(anyLong());

            // when & then
            mockMvc.perform(delete("/api/table-groups/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
