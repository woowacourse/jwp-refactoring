package kitchenpos.documentation;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

class MenuGroupRestControllerTest extends DocumentationTest {
    private static final String MENU_GROUPS_API_URL = "/api/menu-groups";

    @DisplayName("POST /api/menu-groups")
    @Test
    void create() {
        given(menuGroupService.create(any()))
                .willReturn(new MenuGroup(1L, "두 마리 메뉴"));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MenuGroupCreateRequest("두 마리 메뉴"))
                .when().post(MENU_GROUPS_API_URL)
                .then().log().all()
                .apply(document("menu-groups/create",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 그룹 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("메뉴 그룹 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("메뉴 그룹 이름")
                        )
                ))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("GET /api/menu-groups")
    @Test
    void list() {
        given(menuGroupService.list())
                .willReturn(List.of(
                        new MenuGroup(1L, "두 마리 메뉴"),
                        new MenuGroup(2L, "세 마리 메뉴"))
                );

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(MENU_GROUPS_API_URL)
                .then().log().all()
                .apply(document("menu-groups/list",
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("메뉴 그룹 아이디"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("메뉴 그룹 이름")
                        )
                ))
                .statusCode(HttpStatus.OK.value());
    }
}
