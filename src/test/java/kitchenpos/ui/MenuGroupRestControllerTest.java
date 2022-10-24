package kitchenpos.ui;

import static javax.management.openmbean.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.application.MenuGroupService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
@DisplayName("MenuGroupRestController 는 ")
class MenuGroupRestControllerTest extends ControllerTest {

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() throws Exception {
        when(menuGroupService.create(any(MenuGroup.class))).thenReturn(DomainFixture.getMenuGroup());

        ResultActions resultActions = mockMvc.perform(post("/api/menu-groups")
                        .content(objectMapper.writeValueAsString(RequestBody.MENU_GROUP))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andDo(document("menu-group/create",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("name").type(STRING).description("menu group name")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("menu group id"),
                        fieldWithPath("name").type(STRING).description("menu group name")
                )));
    }

    @DisplayName("메뉴 그룹들을 가져온다.")
    @Test
    void getMenuGroups() throws Exception {
        when(menuGroupService.list()).thenReturn(List.of(DomainFixture.getMenuGroup()));

        ResultActions resultActions = mockMvc.perform(get("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("menu-group/get-menu-groups",
                getResponsePreprocessor(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("menu group id"),
                        fieldWithPath("[].name").type(STRING).description("menu group name")
                )));
    }
}
