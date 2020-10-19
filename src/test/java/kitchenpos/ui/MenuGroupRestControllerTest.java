package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends MvcTest {

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("/api/menu-groups로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(menuGroupService.create(any())).willReturn(MENU_GROUP_1);

        String inputJson = objectMapper.writeValueAsString(MENU_GROUP_1);
        MvcResult mvcResult = postAction("/api/menu-groups", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/menu-groups/1"))
            .andReturn();

        MenuGroup menuGroupResponse =
            objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuGroup.class);
        assertThat(menuGroupResponse).usingRecursiveComparison().isEqualTo(MENU_GROUP_1);
    }

    @DisplayName("/api/menu-groups로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(menuGroupService.list()).willReturn(Arrays.asList(MENU_GROUP_1, MENU_GROUP_2));

        MvcResult mvcResult = getAction("/api/menu-groups")
            .andExpect(status().isOk())
            .andReturn();

        List<MenuGroup> menuGroupsResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<MenuGroup>>() {});
        assertAll(
            () -> assertThat(menuGroupsResponse.size()).isEqualTo(2),
            () -> assertThat(menuGroupsResponse.get(0)).usingRecursiveComparison().isEqualTo(MENU_GROUP_1),
            () -> assertThat(menuGroupsResponse.get(1)).usingRecursiveComparison().isEqualTo(MENU_GROUP_2)
        );
    }
}