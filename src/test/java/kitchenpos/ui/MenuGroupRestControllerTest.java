package kitchenpos.ui;

import static kitchenpos.support.MenuGroupFixtures.MENU_GROUP1;
import static kitchenpos.support.MenuGroupFixtures.createAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("menu group을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final MenuGroup menuGroup = MENU_GROUP1.create();

        given(menuGroupService.create(any(MenuGroup.class))).willReturn(menuGroup);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuGroup)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("menu group들을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final List<MenuGroup> menuGroups = createAll();

        given(menuGroupService.list()).willReturn(menuGroups);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/menu-groups")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }
}
