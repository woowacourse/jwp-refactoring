package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static kitchenpos.fixture.MenuGroupFixture.RESPONSE;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.builder()
                .id(1L)
                .name("한식")
                .build();
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 메뉴_그룹_생성() throws Exception {
            // given
            given(menuGroupService.create(any(CreateMenuGroupRequest.class)))
                    .willReturn(RESPONSE.메뉴_그룹_치킨_생성_응답());

            // when & then
            mockMvc.perform(post("/api/menu-groups")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"name\":\"치킨\"" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").exists(),
                            jsonPath("name").value("치킨")
                    );
        }

        @Test
        void 메뉴_그룹_목록_조회() throws Exception {
            // when & then
            mockMvc.perform(get("/api/menu-groups"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$").isArray()
                    );
        }
    }
}
