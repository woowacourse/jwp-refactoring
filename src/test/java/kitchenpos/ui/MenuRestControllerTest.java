package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        // given
        final Menu menu = new Menu();
        menu.setName("치킨");
        menu.setPrice(BigDecimal.valueOf(10000));
        menu.setMenuGroupId(1L);
        given(menuService.create(any(Menu.class))).willReturn(menu);

        // when
        final MockHttpServletResponse response = mockMvc.perform(
                        post("/api/menus")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(menu))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getContentAsString()).isNotEmpty();
    }

    @Test
    @DisplayName("GET /api/menus - 모든 Menu 조회")
    public void list() throws Exception {
        // given
        final Menu menu1 = new Menu();
        menu1.setPrice(BigDecimal.valueOf(10000));
        given(menuService.list()).willReturn(List.of(
                menu1,
                new Menu()
        ));

        // when
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/menus")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getContentAsString()).contains("10000");
    }
}
