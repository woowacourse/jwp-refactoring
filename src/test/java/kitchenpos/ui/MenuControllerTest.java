package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuProductResponse;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends ControllerTest {

    @DisplayName("POST /api/menus")
    @Test
    void create() throws Exception {
        // given
        MenuRequest menuRequest = new MenuRequest("후라이드+후라이드", 19000, 1L,
                List.of(new MenuProductRequest(1, 2)));
        MenuResponse menuResponse = new MenuResponse(1L, "후라이드+후라이드", 19000, 1L,
                List.of(new MenuProductResponse(1L, 1L, 1L, 2)));

        given(menuApiService.create(any(MenuRequest.class)))
                .willReturn(menuResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + menuResponse.getId()));
    }

    @DisplayName("GET /api/menus")
    @Test
    void list() throws Exception {
        // given
        List<MenuResponse> menuResponses = List.of(
                new MenuResponse(1L, "메뉴1", 1000, 1L,
                        List.of(new MenuProductResponse(1L, 1L, 1L, 2))),
                new MenuResponse(1L, "메뉴1", 1000, 1L,
                        List.of(new MenuProductResponse(1L, 1L, 1L, 2)))
        );
        given(menuApiService.list()).willReturn(menuResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/menus"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(menuResponses)));
    }
}
