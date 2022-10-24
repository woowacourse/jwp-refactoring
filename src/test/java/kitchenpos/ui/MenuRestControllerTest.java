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
import kitchenpos.application.MenuService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
@DisplayName("MenuRestController 는 ")
class MenuRestControllerTest extends ControllerTest {

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성해야 한다.")
    @Test
    void createMenu() throws Exception {
        when(menuService.create(any(Menu.class))).thenReturn(DomainFixture.getMenu());

        ResultActions resultActions = mockMvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(RequestBody.getMenuProductFixture(1L, 1L)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        resultActions.andDo(document("menu/create",
                getRequestPreprocessor(),
                getResponsePreprocessor(),
                requestFields(
                        fieldWithPath("name").type(STRING).description("menu name"),
                        fieldWithPath("price").type(NUMBER).description("menu price"),
                        fieldWithPath("menuGroupId").type(NUMBER).description("menu group id"),
                        fieldWithPath("menuProducts.[].productId").type(NUMBER).description("menu product id"),
                        fieldWithPath("menuProducts.[].quantity").type(NUMBER).description("menu product quantity")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("menu id"),
                        fieldWithPath("name").type(STRING).description("menu name"),
                        fieldWithPath("price").type(NUMBER).description("menu price"),
                        fieldWithPath("menuGroupId").type(NUMBER).description("menu group id"),
                        fieldWithPath("menuProducts.[].id").type(NUMBER).description("menu product id"),
                        fieldWithPath("menuProducts.[].productId").type(NUMBER).description("menu product id"),
                        fieldWithPath("menuProducts.[].quantity").type(NUMBER).description("menu product quantity"),
                        fieldWithPath("menuProducts.[].menuId").type(NUMBER).description("menu id in menu product")
                )));
    }

    @DisplayName("메뉴 항목을 조회한다.")
    @Test
    void getMenus() throws Exception {
        when(menuService.list()).thenReturn(List.of(DomainFixture.getMenu()));

        ResultActions resultActions = mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        resultActions.andDo(document("menu/get-menus",
                getResponsePreprocessor(),
                responseFields(
                        fieldWithPath("[].id").type(NUMBER).description("menu id"),
                        fieldWithPath("[].name").type(STRING).description("menu name"),
                        fieldWithPath("[].price").type(NUMBER).description("menu price"),
                        fieldWithPath("[].menuGroupId").type(NUMBER).description("menu group id"),
                        fieldWithPath("[].menuProducts.[].id").type(NUMBER).description("menu product id"),
                        fieldWithPath("[].menuProducts.[].productId").type(NUMBER).description("menu product id"),
                        fieldWithPath("[].menuProducts.[].quantity").type(NUMBER).description("menu product quantity"),
                        fieldWithPath("[].menuProducts.[].menuId").type(NUMBER).description("menu id in menu product")
                )));
    }
}
