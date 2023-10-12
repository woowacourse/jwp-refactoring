package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(16000L));
        menu.setMenuGroupId(1L);
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 메뉴_생성() throws Exception {
            // given
            given(menuService.create(any(Menu.class)))
                    .willReturn(menu);

            // when & then
            mockMvc.perform(post("/api/menus")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"name\":\"후라이드치킨\"," +
                                    "\"price\":16000," +
                                    "\"menuGroupId\":1" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").value(menu.getId()),
                            jsonPath("name").value(menu.getName()),
                            jsonPath("price").value(menu.getPrice()),
                            jsonPath("menuGroupId").value(menu.getMenuGroupId())
                    );
        }

        @Test
        void 메뉴_목록_조회() throws Exception {
            // given
            given(menuService.list())
                    .willReturn(List.of(menu));

            // when & then
            mockMvc.perform(get("/api/menus"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$[0].id").value(menu.getId()),
                            jsonPath("$[0].name").value(menu.getName()),
                            jsonPath("$[0].price").value(menu.getPrice()),
                            jsonPath("$[0].menuGroupId").value(menu.getMenuGroupId())
                    );
        }
    }
}
