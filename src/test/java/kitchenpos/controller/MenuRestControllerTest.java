package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.ui.MenuRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 생성하기")
    @Test
    void createMenu() throws Exception {
        Menu menu = Fixtures.makeMenu();

        ObjectMapper objectMapper = new ObjectMapper();

        given(menuService.create(any(MenuRequest.class)))
            .willReturn(menu);

        String content = objectMapper.writeValueAsString(menu);

        mvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 불러오기")
    @Test
    void listMenu() throws Exception {
        List<Menu> menus = new ArrayList<>();

        given(menuService.list())
            .willReturn(menus);

        mvc.perform(get("/api/menus")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
