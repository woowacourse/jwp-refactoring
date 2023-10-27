package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.Price;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.ui.MenuRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MenuRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() throws Exception {
        // given
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "반반 치킨세트",
                BigDecimal.valueOf(30000),
                1L,
                List.of(new MenuProductCreateRequest(1L, 1), new MenuProductCreateRequest(2L, 1))
        );

        given(menuService.create(any()))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateRequest)));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/menus/1"));
    }

    @DisplayName("이름이 비어있으면 예외 처리한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void create_FailWhenRequestNameIsBlank(final String name) throws Exception {
        // given
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                name,
                BigDecimal.valueOf(30000),
                1L,
                List.of(new MenuProductCreateRequest(1L, 1), new MenuProductCreateRequest(2L, 1))
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("가격이 null이거나 0원 미만이면 예외 처리한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void create_FailWhenRequestPriceIsInvalid(final BigDecimal price) throws Exception {
        // given
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "반반 치킨세트",
                price,
                1L,
                List.of(new MenuProductCreateRequest(1L, 1), new MenuProductCreateRequest(2L, 1))
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴그룹 Id가 null이면 예외 처리한다.")
    @Test
    void create_FailWhenRequestMenuGroupIdIsNull() throws Exception {
        // given
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "반반 치킨세트",
                BigDecimal.valueOf(30000),
                null,
                List.of(new MenuProductCreateRequest(1L, 1), new MenuProductCreateRequest(2L, 1))
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴에 사용되는 상품 목록이 null 예외 처리한다.")
    @Test
    void create_FailWhenRequestMenuProductsIsNull() throws Exception {
        // given
        final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "반반 치킨세트",
                BigDecimal.valueOf(30000),
                1L,
                null
        );

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuCreateRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception{
        // given
        final List<MenuResponse> menuResponses = List.of(
                MenuResponse.from(new Menu("반반 치킨세트", new Price(BigDecimal.valueOf(30000)), 1L)),
                MenuResponse.from(new Menu("간장 양념 치킨세트", new Price(BigDecimal.valueOf(32000)), 2L))
        );

        given(menuService.list())
                .willReturn(menuResponses);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/menus")
                .contentType(APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
    }
}
