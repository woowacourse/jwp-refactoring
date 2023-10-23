package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.application.dto.request.CreateMenuRequest;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.RESPONSE;
import static kitchenpos.fixture.MenuGroupFixture.MENU_GROUP;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        menu = Menu.builder()
                .id(1L)
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(16000L))
                .menuGroup(MENU_GROUP.메뉴_그룹_치킨())
                .build();
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 메뉴_생성() throws Exception {
            // given
            given(menuService.create(any(CreateMenuRequest.class)))
                    .willReturn(RESPONSE.후라이드_치킨_생성_응답());

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
                            jsonPath("menuGroupId").value(menu.getMenuGroup().getId())
                    );
        }

        @Test
        void 메뉴_목록_조회() throws Exception {
            // given
            given(menuService.list())
                    .willReturn(List.of(RESPONSE.후라이드_치킨()));

            // when & then
            mockMvc.perform(get("/api/menus"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray(),
                            jsonPath("$[0].id").value(menu.getId()),
                            jsonPath("$[0].name").value(menu.getName()),
                            jsonPath("$[0].price").value(menu.getPrice()),
                            jsonPath("$[0].menuGroupId").value(menu.getMenuGroup().getId())
                    );
        }
    }
}
