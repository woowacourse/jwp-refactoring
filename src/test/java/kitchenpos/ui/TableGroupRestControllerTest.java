package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.of(2023, Month.APRIL, 1, 0, 0, 0, 0));
        tableGroup.setOrderTables(List.of(orderTable));
    }


    @Nested
    class 정상_요청_테스트 {

        @Test
        void 주문_테이블_그룹_생성() throws Exception {
            // given
            given(tableGroupService.create(any(TableGroup.class)))
                    .willReturn(tableGroup);

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
                            jsonPath("orderTables[0].id").value(orderTable.getId()),
                            jsonPath("orderTables[0].tableGroupId").value(orderTable.getTableGroupId()),
                            jsonPath("orderTables[0].numberOfGuests").value(orderTable.getNumberOfGuests()),
                            jsonPath("orderTables[0].empty").value(orderTable.isEmpty())
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
