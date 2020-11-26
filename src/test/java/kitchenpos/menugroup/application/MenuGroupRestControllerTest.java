package kitchenpos.menugroup.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @DisplayName("정상적인 메뉴그룹 생성 요청에 created 상태로 응답하는지 확인한다.")
    @Test
    void createTest() throws Exception {
        final String name = "메뉴";
        final MenuGroup savedMenuGroup = createMenuGroup(1L, name);
        final MenuGroupCreateRequest menuGroupWithoutId = new MenuGroupCreateRequest(name);

        given(menuGroupService.create(any(MenuGroup.class))).willReturn(savedMenuGroup);

        mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(menuGroupWithoutId))
        )
            .andExpect(status().isCreated())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(MenuGroupResponse.from(savedMenuGroup))))
            .andExpect(header().exists("Location"));
    }

    @DisplayName("정상적인 메뉴 그룹 리스트 요청에 ok상태로 응답하는지 확인한다.")
    @Test
    void listTest() throws Exception {
        final MenuGroup first = createMenuGroup(1L, "두마리메뉴");
        final MenuGroup second = createMenuGroup(2L, "한마리메뉴");
        final MenuGroup third = createMenuGroup(3L, "순살파닭두마리메뉴");
        final MenuGroup fourth = createMenuGroup(4L, "신메뉴");
        final MenuGroup fifth = createMenuGroup(5L, "메뉴");
        final List<MenuGroup> menuGroups = Arrays.asList(first, second, third, fourth, fifth);
        final List<MenuGroupResponse> menuGroupResponses = menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());

        given(menuGroupService.list()).willReturn(menuGroups);

        mockMvc.perform(get("/api/menu-groups"))
            .andExpect(status().isOk())
            .andExpect(content().bytes(objectMapper.writeValueAsBytes(menuGroupResponses)));
    }
}
