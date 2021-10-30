package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menugroup.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("MenuGroup 통합테스트")
class MenuGroupIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/menu-groups";

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menuGroupRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.name").value(menuGroupRequest.getName()))
        ;

        final List<MenuGroup> foundAllMenuGroups = menuGroupRepository.findAll();
        assertThat(foundAllMenuGroups).hasSize(1);

        final MenuGroup foundMenuGroup = foundAllMenuGroups.get(0);
        assertThat(foundMenuGroup.getName()).isEqualTo(menuGroupRequest.getName());
    }

    @DisplayName("모든 MenuGroup들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final MenuGroup menuGroup1 = MenuGroup을_저장한다("한마리메뉴");
        final MenuGroup menuGroup2 = MenuGroup을_저장한다("두마리메뉴");

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[0].name").value(menuGroup1.getName()))
            .andExpect(jsonPath("$[1].id").isNumber())
            .andExpect(jsonPath("$[1].name").value(menuGroup2.getName()))
        ;
    }
}
