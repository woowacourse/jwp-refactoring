package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuRestControllerTest extends ControllerTest {

    @Test
    void 메뉴_생성() throws Exception {
        // given
        Menu menu = 메뉴();
        String request = objectMapper.writeValueAsString(menu);
        Menu savedMenu = 메뉴(1L);
        given(menuService.create(any())).willReturn(savedMenu);
        String response = objectMapper.writeValueAsString(savedMenu);

        // when & then
        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 메뉴_조회() throws Exception {
        // given
        Menu 메뉴1 = 메뉴(1L);
        Menu 메뉴2 = 메뉴(2L);
        List<Menu> menus = List.of(메뉴1, 메뉴2);
        given(menuService.list()).willReturn(menus);
        String response = objectMapper.writeValueAsString(menus);

        // when & then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
