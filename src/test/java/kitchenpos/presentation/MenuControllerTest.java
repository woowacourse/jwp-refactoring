package kitchenpos.presentation;

import static kitchenpos.fixture.dto.MenuGroupDtoFixture.메뉴그룹A_응답;
import static kitchenpos.fixture.dto.MenuGroupDtoFixture.메뉴그룹B_응답;
import static kitchenpos.fixture.dto.MenuGroupDtoFixture.메뉴그룹C_응답;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuProductResponse;
import kitchenpos.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class MenuControllerTest extends ControllerTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(
                new MenuRequest("짜장면", BigDecimal.valueOf(8_000L), 1L, List.of(new MenuProductRequest(1L, 2)))
        );

        given(menuService.create(any()))
                .willReturn(new MenuResponse(1L, "짜장면", BigDecimal.valueOf(8_000L), 1L,
                        List.of(new MenuProductResponse(1L, 1L, 1L, 2))));

        // when
        final ResultActions perform = mockMvc.perform(
                        post("/api/menus")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()));
    }

    @Test
    @DisplayName("메뉴를 조회한다.")
    void list() throws Exception {
        // given
        given(menuGroupService.list())
                .willReturn(List.of(메뉴그룹A_응답, 메뉴그룹B_응답, 메뉴그룹C_응답));

        // when
        final ResultActions perform = mockMvc.perform(get("/api/menu-groups"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
