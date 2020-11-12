package kitchenpos.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuRestIntegrationTest extends IntegrationTest {
    @DisplayName("1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        MenuProduct friedChicken =
                TestObjectUtils.createMenuProduct(3L, null, 1L, 1L);
        MenuProduct SeasoningChicken =
                TestObjectUtils.createMenuProduct(4L, null, 2L, 1L);
        Menu createMenu = TestObjectUtils.createMenu(null, "두마리치킨",
                BigDecimal.valueOf(16000), 1L,
                Arrays.asList(friedChicken, SeasoningChicken));
        String createMenuJson = objectMapper.writeValueAsString(createMenu);

        mockMvc.perform(
                post("/api/menus")
                        .content(createMenuJson)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(createMenu.getName()))
                .andExpect(jsonPath("price").value(createMenu.getPrice().longValue()))
                .andExpect(jsonPath("menuGroupId").value(createMenu.getMenuGroupId()))
                .andExpect(jsonPath("menuProducts[0].seq").exists())
                .andExpect(jsonPath("menuProducts[0].menuId").exists())
                .andExpect(jsonPath("menuProducts[0].productId").exists())
                .andExpect(jsonPath("menuProducts[0].quantity").value(
                        createMenu.getMenuProducts().get(0).getQuantity()));

    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/api/menus")
        )
                .andExpect(status().isOk())
                .andReturn();

        List<MenuGroup> menuGroups = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, MenuGroup.class));

        assertThat(menuGroups.size()).isEqualTo(6);
    }
}
