package kitchenpos.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class TableRestControllerTest extends DocumentationTest {
    private static final String TABLE_API_URL = "/api/tables";

    @DisplayName("POST " + TABLE_API_URL)
    @Test
    void create() {
        final var empty = true;
        final var numberOfGuests = 0;
        given(tableService.create(any()))
                .willReturn(new OrderTable(1L, null, numberOfGuests, empty));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTableCreateRequest(numberOfGuests, empty))
                .when().post(TABLE_API_URL)
                .then().log().all()
                .apply(document("tables/create",
                        requestFields(
                                fieldWithPath("numberOfGuests").type(JsonFieldType.NUMBER).description("고객 인원 수"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("true:주문불가, false:주문가능")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("테이블 아이디"),
                                fieldWithPath("tableGroupId").type(JsonFieldType.NUMBER).description("테이블 그룹 아이디")
                                        .optional(),
                                fieldWithPath("numberOfGuests").type(JsonFieldType.NUMBER).description("테이블 고객 인원 수"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("테이블 주문 가능 여부")
                        )
                ))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("GET " + TABLE_API_URL)
    @Test
    void list() {
        given(tableService.list())
                .willReturn(
                        List.of(new OrderTable(1L, null, 0, true),
                                new OrderTable(2L, 1L, 4, false),
                                new OrderTable(3L, 1L, 4, false))
                );

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(TABLE_API_URL)
                .then().log().all()
                .apply(document("tables/list",
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("테이블 아이디"),
                                fieldWithPath("[].tableGroupId").type(JsonFieldType.NUMBER).description("테이블 그룹 아이디")
                                        .optional(),
                                fieldWithPath("[].numberOfGuests").type(JsonFieldType.NUMBER)
                                        .description("테이블 고객 인원 수"),
                                fieldWithPath("[].empty").type(JsonFieldType.BOOLEAN).description("테이블 주문 가능 여부")
                        )
                ))
                .statusCode(HttpStatus.OK.value());

    }

    @DisplayName("PUT " + TABLE_API_URL + "/{tableId}/empty")
    @Test
    void changeEmpty() {
        given(tableService.changeEmpty(any(), any()))
                .willReturn(new OrderTable(1L, null, 0, false));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTableChangeEmptyRequest(false))
                .when().put("/api/tables/{tableId}/empty", 1)
                .then().log().all()
                .apply(document("tables/changeEmpty",
                        pathParameters(
                                parameterWithName("tableId").description("수정 대상 테이블 아이디")),
                        requestFields(
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("true:주문불가, false:주문가능")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("테이블 아이디"),
                                fieldWithPath("tableGroupId").type(JsonFieldType.NUMBER).description("테이블 그룹 아이디")
                                        .optional(),
                                fieldWithPath("numberOfGuests").type(JsonFieldType.NUMBER)
                                        .description("테이블 고객 인원 수"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("테이블 주문 가능 여부")
                        )
                ))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("PUT " + TABLE_API_URL + "/{tableId}/number-of-guests")
    @Test
    void changeNumberOfGuests() {
        given(tableService.changeNumberOfGuests(any(), any()))
                .willReturn(new OrderTable(1L, null, 5, false));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTableChangeNumberOfGuestsRequest(5))
                .when().put("/api/tables/{tableId}/number-of-guests", 1)
                .then().log().all()
                .apply(document("tables/changeNumberOfGuests",
                        pathParameters(
                                parameterWithName("tableId").description("수정 대상 테이블 아이디")),
                        requestFields(
                                fieldWithPath("numberOfGuests").type(JsonFieldType.NUMBER).description("설정 희망 고객 인원 수")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("테이블 아이디"),
                                fieldWithPath("tableGroupId").type(JsonFieldType.NUMBER).description("테이블 그룹 아이디")
                                        .optional(),
                                fieldWithPath("numberOfGuests").type(JsonFieldType.NUMBER)
                                        .description("테이블 고객 인원 수"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("테이블 주문 가능 여부")
                        )
                ))
                .statusCode(HttpStatus.OK.value());
    }
}
