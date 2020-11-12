package kitchenpos.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.MenuGroup;

class MenuGroupRestIntegrationTest extends IntegrationTest {
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroupTest() throws Exception {
        MenuGroup createMenuGroup = TestObjectUtils.createMenuGroup(null, "두마리메뉴");
        String createMenuGroupJson = objectMapper.writeValueAsString(createMenuGroup);

        mockMvc.perform(
                post("/api/menu-groups")
                        .content(createMenuGroupJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(createMenuGroup.getName()));
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/menu-groups")
        )
                .andExpect(status().isOk())
                .andReturn();

        List<MenuGroup> menuGroups = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, MenuGroup.class));

        assertThat(menuGroups.size()).isEqualTo(4);
    }
}