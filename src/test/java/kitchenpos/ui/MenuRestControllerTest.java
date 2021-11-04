package kitchenpos.ui;

import kitchenpos.domain.Menu;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 문서화 테스트")
class MenuRestControllerTest extends ApiDocument {
    @DisplayName("메뉴 저장 - 성공")
    @Test
    void menu_create() throws Exception {
        //given
        final Menu requestMenu = new Menu();
        requestMenu.setName("후라이드+후라이드");
        requestMenu.setPrice(new BigDecimal(19000));
        requestMenu.setMenuGroupId(MenuGroupFixture.두마리메뉴.getId());
        requestMenu.setMenuProducts(Collections.singletonList(MenuProductFixture.후라이드후라이드_후라이드치킨_NO_KEY));
        //when
        willReturn(MenuFixture.후라이드_후라이드).given(menuService).create(any(Menu.class));
        final ResultActions result = 메뉴_저장_요청(requestMenu);
        //then
        메뉴_저장_성공함(result, MenuFixture.후라이드_후라이드);
    }

    @DisplayName("메뉴 조회 - 성공")
    @Test
    void menu_findAll() throws Exception {
        //given
        //when
        willReturn(MenuFixture.menus()).given(menuService).list();
        final ResultActions result = 메뉴_조회_요청();
        //then
        메뉴_조회_성공함(result, MenuFixture.menus());
    }

    private ResultActions 메뉴_저장_요청(Menu requestMenu) throws Exception {
        return mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestMenu))
        );
    }

    private ResultActions 메뉴_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void 메뉴_저장_성공함(ResultActions result, Menu responseMenu) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + responseMenu.getId()))
                .andDo(toDocument("menu-create"));
    }

    private void 메뉴_조회_성공함(ResultActions result, List<Menu> menus) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(menus)))
                .andDo(toDocument("menu-findAll"));
    }
}