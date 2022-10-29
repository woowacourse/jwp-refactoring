package kitchenpos.ui;

import static kitchenpos.fixture.domain.MenuFixture.createMenu;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.application.MenuService;
import kitchenpos.ui.dto.request.MenuProductCreateRequest;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest extends ControllerTest {

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    public void create() throws Exception {
        // given
        MenuCreateRequest request = new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(20_000L), 1L,
                Collections.singletonList(new MenuProductCreateRequest(1L, 2)));
        given(menuService.create(any())).willReturn(createMenu());

        // when
        ResultActions perform = mockMvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated());
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    public void list() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
