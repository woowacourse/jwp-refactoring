package kitchenpos.ui.menu;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.CreateMenuResponse;
import kitchenpos.menu.application.dto.SearchMenuResponse;
import kitchenpos.menu.ui.dto.CreateMenuRequest;
import kitchenpos.menu.ui.dto.MenuProductRequest;
import kitchenpos.ui.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuRestControllerTest extends ControllerTest {

    @Test
    void 메뉴_생성() throws Exception {
        // given
        CreateMenuRequest createMenuRequest = new CreateMenuRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                List.of(new MenuProductRequest(1L, 2))
        );
        String request = objectMapper.writeValueAsString(createMenuRequest);

        CreateMenuResponse createMenuResponse = 메뉴_응답();
        given(menuService.create(any())).willReturn(createMenuResponse);
        String response = objectMapper.writeValueAsString(createMenuResponse);

        // when & then
        mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 메뉴_조회() throws Exception {
        // given
        SearchMenuResponse searchMenuResponse1 = 메뉴_조회_응답();
        SearchMenuResponse searchMenuResponse2 = 메뉴_조회_응답();
        List<SearchMenuResponse> searchMenuResponses = List.of(searchMenuResponse1, searchMenuResponse2);
        given(menuService.list()).willReturn(searchMenuResponses);
        String response = objectMapper.writeValueAsString(searchMenuResponses);

        // when & then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
