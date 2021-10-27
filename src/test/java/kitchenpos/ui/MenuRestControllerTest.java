package kitchenpos.ui;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.CreateMenuRequest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("MenuRestController 단위 테스트")
class MenuRestControllerTest extends ControllerTest {

    private MenuProduct 후라이드치킨;
    private MenuProduct 양념치킨;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        후라이드치킨 = new MenuProduct(1L, 1);
        양념치킨 = new MenuProduct(2L, 1);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다")
    void create() throws Exception {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(30000), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        MenuProduct expected_후라이드치킨 = new MenuProduct(1L, 1L, 1L, 1);
        MenuProduct expected_양념치킨 = new MenuProduct(2L, 1L, 2L, 1);
        Menu expected = new Menu(1L, "양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(expected_후라이드치킨, expected_양념치킨));
        given(menuService.create(any(CreateMenuRequest.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(post("/api/menus")
                .content(objectToJsonString(양념반_후라이드반))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/menus/" + expected.getId()))
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("메뉴의 가격이 null이면 메뉴를 등록할 수 없다.")
    void createWrongPriceNull() throws Exception {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", null, 1L, Arrays.asList(후라이드치킨, 양념치킨));
        willThrow(new IllegalArgumentException("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다."))
                .given(menuService)
                .create(any(CreateMenuRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/menus/")
                .content(objectToJsonString(양념반_후라이드반))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("메뉴의 가격이 음수면 메뉴를 등록할 수 없다.")
    void createWrongPriceUnderZero() throws Exception {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(-1), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        willThrow(new IllegalArgumentException("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다."))
                .given(menuService)
                .create(any(CreateMenuRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/menus/")
                .content(objectToJsonString(양념반_후라이드반))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 비어있을 수 없고 0 이상이어야 합니다."));
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 등록할 수 없다.")
    void createWrongPriceSumOfProducts() throws Exception {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(32001), 1L, Arrays.asList(후라이드치킨, 양념치킨));
        willThrow(new IllegalArgumentException("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다."))
                .given(menuService)
                .create(any(CreateMenuRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/menus/")
                .content(objectToJsonString(양념반_후라이드반))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다."));
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongMenuGroupNotExist() throws Exception {
        // given
        CreateMenuRequest 양념반_후라이드반 = new CreateMenuRequest("양념 반 + 후라이드 반", BigDecimal.valueOf(32000), 10L, Arrays.asList(후라이드치킨, 양념치킨));
        willThrow(new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."))
                .given(menuService)
                .create(any(CreateMenuRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/menus/")
                .content(objectToJsonString(양념반_후라이드반))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("메뉴 그룹이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("목록에 포함된 데이터들이 존재하지 않으면 메뉴를 등록할 수 없다.")
    void createWrongProductNotExist() throws Exception {
        // given
        MenuProduct 간장치킨 = new MenuProduct(10L, 1);
        CreateMenuRequest 간장반_후라이드반 = new CreateMenuRequest("간장 반 + 후라이드 반", BigDecimal.valueOf(32000), 1L, Arrays.asList(후라이드치킨, 간장치킨));
        willThrow(new IllegalArgumentException("상품이 존재하지 않습니다.")).given(menuService)
                                                                 .create(any(CreateMenuRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/menus/")
                .content(objectToJsonString(간장반_후라이드반))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("상품이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("전체 메뉴를 조회할 수 있다")
    void list() throws Exception {
        // given
        MenuProduct 후라이드치킨1 = new MenuProduct(1L, 1L, 3L, 1);
        MenuProduct 양념치킨 = new MenuProduct(2L, 1L, 3L, 1);
        MenuProduct 후라이드치킨2 = new MenuProduct(3L, 2L, 3L, 1);
        MenuProduct 간장치킨 = new MenuProduct(4L, 2L, 3L, 1);
        Menu 양념반_후라이드반 = new Menu(1L, "양념 반 + 후라이드 반", 30000, 1L, Arrays.asList(후라이드치킨1, 양념치킨));
        Menu 간장반_후라이드반 = new Menu(2L, "간장 반 + 후라이드 반", 30000, 2L, Arrays.asList(후라이드치킨2, 간장치킨));
        List<Menu> expected = Arrays.asList(양념반_후라이드반, 간장반_후라이드반);
        given(menuService.list()).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }
}
