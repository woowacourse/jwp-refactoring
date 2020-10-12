package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends MvcTest {

    private static final long MENU_PRODUCT_SEQ_1 = 1L;
    private static final long MENU_PRODUCT_PRODUCT_ID_1 = 1L;
    private static final long MENU_PRODUCT_QUANTITY_1 = 1L;
    private static final MenuProduct MENU_PRODUCT_1 = new MenuProduct();

    private static final long MENU_PRODUCT_SEQ_2 = 2L;
    private static final long MENU_PRODUCT_PRODUCT_ID_2 = 2L;
    private static final long MENU_PRODUCT_QUANTITY_2 = 2L;
    private static final MenuProduct MENU_PRODUCT_2 = new MenuProduct();

    private static final int MENU_ID_1 = 1;
    private static final String MENU_NAME_1 = "메뉴이름1";
    private static final BigDecimal MENU_PRICE_1 = new BigDecimal(1);
    private static final long MENU_GROUP_ID_1 = 1L;
    private static final Menu MENU_1 = new Menu();

    private static final int MENU_ID_2 = 2;
    private static final String MENU_NAME_2 = "메뉴이름2";
    private static final BigDecimal MENU_PRICE_2 = new BigDecimal(2);
    private static final long MENU_GROUP_ID_2 = 2L;
    private static final Menu MENU_2 = new Menu();

    @MockBean
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        MENU_PRODUCT_1.setSeq(MENU_PRODUCT_SEQ_1);
        MENU_PRODUCT_1.setMenuId((long)MENU_ID_1);
        MENU_PRODUCT_1.setProductId(MENU_PRODUCT_PRODUCT_ID_1);
        MENU_PRODUCT_1.setQuantity(MENU_PRODUCT_QUANTITY_1);

        MENU_PRODUCT_2.setSeq(MENU_PRODUCT_SEQ_2);
        MENU_PRODUCT_2.setMenuId((long)MENU_ID_2);
        MENU_PRODUCT_2.setProductId(MENU_PRODUCT_PRODUCT_ID_2);
        MENU_PRODUCT_2.setQuantity(MENU_PRODUCT_QUANTITY_2);

        MENU_1.setId((long)MENU_ID_1);
        MENU_1.setName(MENU_NAME_1);
        MENU_1.setPrice(MENU_PRICE_1);
        MENU_1.setMenuGroupId(MENU_GROUP_ID_1);
        MENU_1.setMenuProducts(Arrays.asList(MENU_PRODUCT_1));

        MENU_2.setId((long)MENU_ID_2);
        MENU_2.setName(MENU_NAME_2);
        MENU_2.setPrice(MENU_PRICE_2);
        MENU_2.setMenuGroupId(MENU_GROUP_ID_2);
        MENU_2.setMenuProducts(Arrays.asList(MENU_PRODUCT_2));
    }

    @DisplayName("/api/menus로 POST요청 성공 테스트")
    @Test
    void createTest() throws Exception {
        given(menuService.create(any())).willReturn(MENU_1);

        String inputJson = objectMapper.writeValueAsString(MENU_1);
        MvcResult mvcResult = postAction("/api/menus", inputJson)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/menus/1"))
            .andReturn();

        Menu menuResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Menu.class);
        assertThat(menuResponse).usingRecursiveComparison().isEqualTo(MENU_1);
    }

    @DisplayName("/api/menus로 GET요청 성공 테스트")
    @Test
    void listTest() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(MENU_1, MENU_2));

        MvcResult mvcResult = getAction("/api/menus")
            .andExpect(status().isOk())
            .andReturn();

        List<Menu> menusResponse = objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(),
            new TypeReference<List<Menu>>() {});
        assertAll(
            () -> assertThat(menusResponse.size()).isEqualTo(2),
            () -> assertThat(menusResponse.get(0)).usingRecursiveComparison().isEqualTo(MENU_1),
            () -> assertThat(menusResponse.get(1)).usingRecursiveComparison().isEqualTo(MENU_2)
        );
    }
}