package kitchenpos.menu.ui;

import static kitchenpos.fixture.MenuFixture.CHICKEN_SET_MENU;
import static kitchenpos.fixture.MenuFixture.CHICKEN_SET_MENU_NON_ID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.ui.MenuRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @Test
    void create() throws Exception {
        // given
        given(menuService.create(any())).willReturn(CHICKEN_SET_MENU);

        // when & then
        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CHICKEN_SET_MENU_NON_ID)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/menus/1"));
    }

    @Test
    void list() throws Exception {
        // given
        given(menuService.list()).willReturn(List.of(CHICKEN_SET_MENU));

        // when & then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(CHICKEN_SET_MENU.getId()))
                .andExpect(jsonPath("$[0].name").value(CHICKEN_SET_MENU.getName()))
                .andExpect(jsonPath("$[0].price").value(CHICKEN_SET_MENU.getPrice()))
                .andExpect(jsonPath("$[0].menuGroupId").value(CHICKEN_SET_MENU.getMenuGroupId()))
                .andExpect(jsonPath("$[0].menuProducts.size()").value(CHICKEN_SET_MENU.getMenuProducts().size()));
    }
}
