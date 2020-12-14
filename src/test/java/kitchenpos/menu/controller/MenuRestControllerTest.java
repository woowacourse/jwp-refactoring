package kitchenpos.menu.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

class MenuRestControllerTest extends ControllerTest {

    @Autowired
    private ProductRepository productDao;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("create: 메뉴 등록 테스트")
    @Test
    void createTest() throws Exception {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(10000)));
        final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 2L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        final MenuRequest menu = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId(),
                Collections.singletonList(menuProduct));

        create("/api/menus", menu)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(menu.getName()));
    }

    @DisplayName("list: 전체 메뉴 조회 테스트")
    @Test
    void listTest() throws Exception {
        final Product product = productDao.save(new Product("후라이드", BigDecimal.valueOf(8000)));
        final MenuProduct menuProduct = new MenuProduct(product, 2L);
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        menuRepository.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(16000), menuGroup,
                Collections.singletonList(menuProduct)));

        findList("/api/menus")
                .andExpect(jsonPath("$[0].name").value("후라이드+후라이드"));
    }
}
