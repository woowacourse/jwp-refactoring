package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 그룹 문서화 테스트")
class MenuGroupRestControllerTest extends ApiDocument {
    @DisplayName("메뉴 그룹 저장 - 성공")
    @Test
    void menu_group_create() throws Exception {
        //given
        final MenuGroup requestMenuGroup = new MenuGroup();
        requestMenuGroup.setName("신메뉴");
        //when
        willReturn(MenuGroupFixture.신메뉴).given(menuGroupService).create(any(MenuGroup.class));
        final ResultActions result = 메뉴_그룹_저장_요청(requestMenuGroup);
        //then
        메뉴_그룹_저장_성공함(result, MenuGroupFixture.신메뉴);
    }

    @DisplayName("메뉴 그룹 조회 - 성공")
    @Test
    void menu_group_findAll() throws Exception {
        //given
        //when
        willReturn(MenuGroupFixture.menuGroups()).given(menuGroupService).list();
        final ResultActions result = 메뉴_그룹_조회_요청();
        //then
        메뉴_그룹_조회_성공함(result, MenuGroupFixture.menuGroups());
    }

    private ResultActions 메뉴_그룹_저장_요청(MenuGroup requestMenuGroup) throws Exception {
        return mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestMenuGroup))
        );
    }

    private ResultActions 메뉴_그룹_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions 메뉴_그룹_저장_성공함(ResultActions result, MenuGroup responseMenuGroup) throws Exception {
        return result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(responseMenuGroup)))
                .andExpect(header().string("Location", "/api/menu-groups/" + responseMenuGroup.getId()))
                .andDo(print())
                .andDo(toDocument("menu-group-create"));
    }

    private void 메뉴_그룹_조회_성공함(ResultActions result, List<MenuGroup> responseMenuGroup) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(responseMenuGroup)))
                .andDo(print())
                .andDo(toDocument("menu-group-findAll"));
    }
}