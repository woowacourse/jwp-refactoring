package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ObjectMapperForTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.ui.request.MenuGroupRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ObjectMapperForTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        MenuGroupRequest menuGroup = new MenuGroupRequest("마크 정식");
        MenuGroupResponse expected = new MenuGroupResponse(1L, "마크 정식");
        given(menuGroupService.create(any(MenuGroupRequest.class))).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectToJson(menuGroup))
        );

        //then
        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", String.format("/api/menu-groups/%s", expected.getId())))
            .andExpect(content().json(objectToJson(expected)));
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void readAll() throws Exception {
        //given
        List<MenuGroupResponse> expected = Arrays.asList(
            new MenuGroupResponse(1L, "마크 정식"),
            new MenuGroupResponse(2L, "안주 3종 세트")
        );
        given(menuGroupService.list()).willReturn(expected);

        //when
        ResultActions response = mockMvc.perform(get("/api/menu-groups"));

        //then
        response.andExpect(status().isOk())
            .andExpect(content().json(objectToJson(expected)));
    }
}
