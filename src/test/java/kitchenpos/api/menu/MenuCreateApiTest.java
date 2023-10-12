package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuCreateApiTest extends ApiTestConfig {

    @DisplayName("메뉴 생성 API 테스트")
    @Test
    void createMenu() throws Exception {
        // given
        final String request = "{\n" +
                "  \"name\": \"후라이드+후라이드\",\n" +
                "  \"price\": 19000,\n" +
                "  \"menuGroupId\": 1,\n" +
                "  \"menuProducts\": [\n" +
                "    {\n" +
                "      \"productId\": 1,\n" +
                "      \"quantity\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long expectedId = 1L;
        final Menu expectedMenu = new Menu();
        expectedMenu.setId(expectedId);
        when(menuService.create(any(Menu.class))).thenReturn(expectedMenu);

        // then
        mockMvc.perform(post("/api/menus")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/menus/%d", expectedId)));
    }
}
