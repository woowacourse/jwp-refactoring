package kitchenpos.ui;

import static javax.management.openmbean.SimpleType.BOOLEAN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.application.TableService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
@DisplayName("TableRestController 는 ")
class TableRestControllerTest extends ControllerTest {

    @MockBean
    private TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() throws Exception {
        when(tableService.create(any(OrderTable.class))).thenReturn(DomainFixture.getOrderTable(true));

        ResultActions resultActions = mockMvc.perform(post("/api/tables")
                        .content(objectMapper.writeValueAsString(RequestBody.ORDER_TABLE_1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andDo(document("order-table/create-order-table",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("numberOfGuests").type(NUMBER).description("number of guest"),
                        fieldWithPath("empty").type(BOOLEAN).description("check empty")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("table order id"),
                        fieldWithPath("tableGroupId").type(NUMBER).description("table group id").optional(),
                        fieldWithPath("numberOfGuests").type(NUMBER).description("the number of guests"),
                        fieldWithPath("empty").type(BOOLEAN).description("check empty")
                )
        ));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void getOrderTables() throws Exception {
        when(tableService.list()).thenReturn(List.of(DomainFixture.getOrderTable(true)));

        ResultActions resultActions = mockMvc.perform(get("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("order-table/get-order-tables",
                getResponsePreprocessor(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("table order id"),
                        fieldWithPath("[].tableGroupId").type(NUMBER).description("table group id").optional(),
                        fieldWithPath("[].numberOfGuests").type(NUMBER).description("the number of guests"),
                        fieldWithPath("[].empty").type(BOOLEAN).description("check empty")
                )
        ));
    }

    @DisplayName("주문 테이블 사용 중 여부를 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        when(tableService.changeEmpty(anyLong(), any(OrderTable.class))).thenReturn(DomainFixture.getOrderTable(true));

        ResultActions resultActions = mockMvc.perform(put("/api/tables/1/empty")
                        .content(objectMapper.writeValueAsString(Map.of("isEmpty", true)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("order-table/change-order-table-empty-status",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("isEmpty").type(BOOLEAN).description("table empty status")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("table order id"),
                        fieldWithPath("tableGroupId").type(NUMBER).description("table group id").optional(),
                        fieldWithPath("numberOfGuests").type(NUMBER).description("the number of guests"),
                        fieldWithPath("empty").type(BOOLEAN).description("check empty")
                )
        ));
    }

    @DisplayName("주문 테이블의 게스트 수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class))).thenReturn(DomainFixture.getOrderTable(true));

        ResultActions resultActions = mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .content(objectMapper.writeValueAsString(Map.of("numberOfGuests", 0)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("order-table/change-the-number-of-guests-in-order-table",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("numberOfGuests").type(NUMBER).description("the number of guests in order table")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("table order id"),
                        fieldWithPath("tableGroupId").type(NUMBER).description("table group id").optional(),
                        fieldWithPath("numberOfGuests").type(NUMBER).description("the number of guests"),
                        fieldWithPath("empty").type(BOOLEAN).description("check empty")
                )
        ));
    }

}
