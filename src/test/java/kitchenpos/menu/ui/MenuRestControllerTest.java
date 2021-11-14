package kitchenpos.menu.ui;

import kitchenpos.support.RestControllerTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.menu.fixture.MenuFixture.createMenuResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends RestControllerTest {

    @MockBean
    private MenuService mockMenuService;

    @DisplayName("메뉴 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        MenuRequest menuRequest = createMenuRequest();
        MenuResponse menuResponse = createMenuResponse(1L, menuRequest);
        when(mockMenuService.create(any())).thenReturn(menuResponse);

        mockMvc.perform(post("/api/menus")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + menuResponse.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(menuResponse)));
    }

    @DisplayName("메뉴 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<MenuResponse> expected = Arrays.asList(createMenuResponse(1L), createMenuResponse(2L));
        when(mockMenuService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
