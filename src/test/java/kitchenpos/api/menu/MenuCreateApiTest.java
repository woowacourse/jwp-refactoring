package kitchenpos.api.menu;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.response.MenuProductResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
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
        final MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(1L, 2);
        final MenuCreateRequest request = new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                List.of(menuProductCreateRequest)
        );

        // when
        final MenuProductResponse menuProductResponse = new MenuProductResponse(1L, menuProductCreateRequest.getQuantity(), menuProductCreateRequest.getProductId());
        final MenuResponse response = new MenuResponse(1L, request.getName(), request.getPrice(), request.getMenuGroupId(), List.of(menuProductResponse));

        when(menuService.create(eq(request))).thenReturn(response);

        // then
        mockMvc.perform(post("/api/menus")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/menus/%d", response.getId())));
    }
}
