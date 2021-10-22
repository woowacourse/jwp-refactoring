package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("MenuGroup 통합테스트")
class MenuGroupIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/menu-groups";

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final Map<String, String> params = new HashMap<>();
        final String menuGroupName = "추천메뉴";
        params.put("name", menuGroupName);

        // when
        // then
        mockMvc.perform(post(API_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(params)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(menuGroupName))
        ;

        final List<MenuGroup> foundAllMenuGroups = menuGroupDao.findAll();
        assertThat(foundAllMenuGroups).hasSize(1);

        final MenuGroup foundMenuGroup = foundAllMenuGroups.get(0);
        assertThat(foundMenuGroup.getId()).isPositive();
        assertThat(foundMenuGroup.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("모든 MenuGroup들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("두마리메뉴");

        final MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("한마리메뉴");

        menuGroupDao.save(menuGroup1);
        menuGroupDao.save(menuGroup2);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[0].name").value("두마리메뉴"))
            .andExpect(jsonPath("$[1].id").isNumber())
            .andExpect(jsonPath("$[1].name").value("한마리메뉴"))
        ;
    }
}
