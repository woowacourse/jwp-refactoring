package kitchenpos.ui;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
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
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("한식");
    }

    @Nested
    class 정상_요청_테스트 {

        @Test
        void 메뉴_그룹_생성() throws Exception {
            // given
            given(menuGroupService.create(any(MenuGroup.class)))
                    .willReturn(menuGroup);

            // when & then
            mockMvc.perform(post("/api/menu-groups")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "\"name\":\"한식\"" +
                                    "}")
                    )
                    .andExpectAll(
                            status().isCreated(),
                            header().exists("Location"),
                            jsonPath("id").exists(),
                            jsonPath("name").value("한식")
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
