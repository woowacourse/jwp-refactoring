package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

    private static final Long MENU_GROUP_ID_1 = 1L;
    private static final Long MENU_GROUP_ID_2 = 2L;
    private static final String MENU_GROUP_NAME_1 = "메뉴그룹1";
    private static final String MENU_GROUP_NAME_2 = "메뉴그룹2";
    private static final MenuGroup MENU_GROUP_1 = new MenuGroup();
    private static final MenuGroup MENU_GROUP_2 = new MenuGroup();

    @MockBean
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        MENU_GROUP_1.setId(MENU_GROUP_ID_1);
        MENU_GROUP_1.setName(MENU_GROUP_NAME_1);
        MENU_GROUP_2.setId(MENU_GROUP_ID_2);
        MENU_GROUP_2.setName(MENU_GROUP_NAME_2);
    }

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