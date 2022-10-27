package kitchenpos.ui;

import static kitchenpos.RequestFixture.MENU_PRODUCT_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.MenuService;
import kitchenpos.application.dto.MenuCreateRequestDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ControllerTestSupporter {

    @MockBean
    private MenuService menuService;

    @Test
    @Disabled
        //TODO: 에러핸들링 하기
    void create_요청_가격이_0이거나_음수이면_에러를_반환한다() throws Exception {
        //given
        MenuCreateRequestDto dto = new MenuCreateRequestDto("name", BigDecimal.valueOf(-100), 1L,
                List.of(MENU_PRODUCT_REQUEST));

        //when
        ResultActions perform = mockMvc.perform(post("api/menus", dto))
                .andDo(print());

        //then
        perform.andExpect(jsonPath("message").value("가격은 0이거나 음수일 수 없습니다."));
    }
}
