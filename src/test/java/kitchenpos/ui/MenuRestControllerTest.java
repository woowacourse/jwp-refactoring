package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.CreateMenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.CreateMenuRequest;
import kitchenpos.ui.dto.MenuProductRequest;
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
        Menu 메뉴1 = 메뉴(1L);
        Menu 메뉴2 = 메뉴(2L);
        List<Menu> menus = List.of(메뉴1, 메뉴2);
        given(menuService.list()).willReturn(menus);
        String response = objectMapper.writeValueAsString(menus);

        // when & then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
