package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MenuService menuService;

    @Test
    @DisplayName("POST /api/menus - Menu 생성")
    public void create() throws Exception {
        //given
        final Menu menu = new Menu(1L, "치킨", Money.valueOf(10000), 1L, null);
        given(menuService.create(any(Menu.class))).willReturn(menu);

        //when & then
        mockMvc.perform(
                        post("/api/menus")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(menu))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value("치킨"))
                .andExpect(jsonPath("price").value(10000))
                .andExpect(jsonPath("menuGroupId").value(1L));
    }

    @Test
    @DisplayName("GET /api/menus - 모든 Menu 조회")
    public void list() throws Exception {
        // given
        final Menu menu1 = new Menu(1L, "치킨", Money.valueOf(10000), 1L, null);
        final Menu menu2 = new Menu(2L, "피자", Money.valueOf(20000), 1L, null);
        given(menuService.list()).willReturn(List.of(
                menu1,
                menu2
        ));

        // when & then
        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("치킨"))
                .andExpect(jsonPath("$[1].name").value("피자"))
                .andExpect(jsonPath("$[0].price").value(10000))
                .andExpect(jsonPath("$[1].price").value(20000));
    }
}
