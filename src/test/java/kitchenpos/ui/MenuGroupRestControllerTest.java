package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends ControllerTest {

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        MenuGroup requestBody = new MenuGroup("추천메뉴");
        given(menuGroupService.create(any())).willReturn(new MenuGroup(1L, "추천메뉴"));

        // when
        ResultActions perform = mockMvc.perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
