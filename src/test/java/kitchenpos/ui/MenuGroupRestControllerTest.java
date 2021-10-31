package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends RestControllerTest {

    @MockBean
    private MenuGroupService mockMenuGroupService;

    @DisplayName("메뉴 그룹 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        MenuGroup requestMenuGroup = createMenuGroup();
        when(mockMenuGroupService.create(any())).then(AdditionalAnswers.returnsFirstArg());
        mockMvc.perform(post("/api/menu-groups")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMenuGroup))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(requestMenuGroup)));
    }

    @DisplayName("메뉴 그룹 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<MenuGroup> expected = Collections.singletonList(createMenuGroup());
        when(mockMenuGroupService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/menu-groups")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
