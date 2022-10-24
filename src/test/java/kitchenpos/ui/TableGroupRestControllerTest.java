package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.application.TableGroupService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableGroupRestController.class)
@DisplayName("TableGroupRestController 는 ")
class TableGroupRestControllerTest extends ControllerTest {

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 지정한다.")
    @Test
    void createGroupTable() throws Exception {
        when(tableGroupService.create(any(TableGroup.class))).thenReturn(DomainFixture.getTableGroup());

        ResultActions resultActions = mockMvc.perform(post("/api/table-groups")
                        .content(objectMapper.writeValueAsString(RequestBody.getOrderTableGroups(1L, 2L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andDo(document("table-group/group-order-table",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("orderTables.[].id").type(NUMBER).description("id of order table"),
                        fieldWithPath("orderTables.[].id").type(NUMBER).description("id of order table")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("id of table group"),
                        fieldWithPath("createdDate").type(STRING).description("created date of table group"),
                        fieldWithPath("orderTables.[].id").type(NUMBER).description("id of order table"),
                        fieldWithPath("orderTables.[].tableGroupId").type(NUMBER).description("id of table group"),
                        fieldWithPath("orderTables.[].numberOfGuests").type(NUMBER).description("the number of guests"),
                        fieldWithPath("orderTables.[].empty").type(BOOLEAN).description("check table is empty")
                )
        ));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroupTable() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/api/table-groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        resultActions.andDo(document("table-group/ungroup-group-table"));
    }

}
