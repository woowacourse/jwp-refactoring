package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = MenuFixture.createWithId(MenuFixture.ID1, 1L,
            Collections.singletonList(MenuProductFixture.create(1L, 1L, 2)), 18000L);
    }

    @DisplayName("정상 Menu 생성")
    @Test
    void create() throws Exception {
        when(menuService.create(any(Menu.class))).thenReturn(menu);

        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(menu))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("location", "/api/menus/" + menu.getId()))
            .andExpect(jsonPath("id").value(menu.getId()));
    }

    @DisplayName("메뉴 불러오기")
    @Test
    void list() throws Exception {
        when(menuService.list()).thenReturn(Collections.singletonList(menu));

        mockMvc.perform(get("/api/menus")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").value(menu.getId()));
    }
}
