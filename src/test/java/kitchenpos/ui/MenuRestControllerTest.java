package kitchenpos.ui;

import static kitchenpos.utils.TestObjects.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuRestControllerTest extends ControllerTest {
    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    @DisplayName("create: 이름을 body message에 포함해 메뉴 등록을 요청시 ,메뉴 생성 성공 시 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        MenuGroup setMenuGroup = menuGroupDao.save(createMenuGroup("세트 그룹"));
        Product product_20_000_won = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(20_000)));
        MenuProduct menuProduct_with_40_000_won = createMenuProduct(null, product_20_000_won.getId(), 2);
        Menu menu_with_price_is_40_000_won = createMenu("후라이드 2마리 세트", BigDecimal.valueOf(40_000), setMenuGroup.getId(),
                Collections.singletonList(menuProduct_with_40_000_won));

        final String createMenuApiUrl = "/api/menus";
        final ResultActions resultActions = create(createMenuApiUrl, menu_with_price_is_40_000_won);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("후라이드 2마리 세트")))
                .andExpect(jsonPath("$.price", is(40_000d)))
                .andExpect(jsonPath("$.menuGroupId", is(1)))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)));
    }

    @DisplayName("list: 전체 메뉴 목록 요청시, 200 응답 코드와 함께 메뉴 목록을 반환한다.")
    @Test
    void list() throws Exception {
        final String findMenusApiUrl = "/api/menus";
        final ResultActions resultActions = findList(findMenusApiUrl);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}