package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        Menu menu = createMenuFixture();

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).usingRecursiveComparison()
            .ignoringFields("id", "menuProducts", "price")
            .isEqualTo(menu);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        Menu menu = createMenuFixture();
        Menu savedMenu = menuService.create(menu);

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).contains(savedMenu);
    }

    private Menu createMenuFixture() {
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");
        Long menuGroupId = menuGroupService.create(menuGroup).getId();

        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        Long productId = productService.create(product).getId();

        return new Menu("후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId,
            List.of(new MenuProduct(productId, 3)));
    }
}
