package kitchenpos.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class TableGroupRestControllerTest extends DocumentationTest {
    private static final String TABLE_GROUP_API_URL = "/api/table-groups";

    @DisplayName("POST " + TABLE_GROUP_API_URL)
    @Test
    void create() {
        given(tableGroupService.create(any()))
                .willReturn(new TableGroup(1L, LocalDateTime.now(), List.of(
                        new OrderTable(3L, 1L, 5, false),
                        new OrderTable(4L, 1L, 5, false)
                )));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TableGroupCreateRequest(List.of(3L, 4L)))
                .when().post(TABLE_GROUP_API_URL)
                .then().log().all()
                .apply(document("table-groups/create",
                        requestFields(
                                fieldWithPath("orderTables.[].id").type(JsonFieldType.NUMBER)
                                        .description("단체 지정 대상 테이블 아이디 목록")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("테이블 그룹 아이디"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("테이블 그룹 생성일시"),
                                fieldWithPath("orderTables.[].id").type(JsonFieldType.NUMBER).description("테이블 아이디"),
                                fieldWithPath("orderTables.[].tableGroupId").type(JsonFieldType.NUMBER)
                                        .description("테이블 그룹 아이디"),
                                fieldWithPath("orderTables.[].numberOfGuests").type(JsonFieldType.NUMBER)
                                        .description("테이블 고객 인원 수"),
                                fieldWithPath("orderTables.[].empty").type(JsonFieldType.BOOLEAN)
                                        .description("테이블 주문 가능 여부")
                        )
                ))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("DELETE " + TABLE_GROUP_API_URL + "/{tableGroupId}")
    @Test
    void ungroup() {
        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", 1)
                .then().log().all()
                .apply(document("table-groups/upgroup",
                        pathParameters(
                                parameterWithName("tableGroupId").description("그룹 해제 대상 테이블 그룹 아이디"))
                ))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
