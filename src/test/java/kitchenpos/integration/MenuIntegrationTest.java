package kitchenpos.integration;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuService;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.product.Product;
import kitchenpos.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuIntegrationTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴가_영속화되면_메뉴프로덕트도_영속화된다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹1"));
        Product product = productService.create(new Product("상품1", BigDecimal.valueOf(1000)));
        MenuProduct menuProduct = new MenuProduct(1L, product.getId());
        Menu menu = new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(menuProduct));
        Menu saved = menuService.create(menu);

        // when, then
        assertThat(menu).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void 메뉴_조회시_메뉴프로덕트도_조회된다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("메뉴그룹1"));
        Product product = productService.create(new Product("상품1", BigDecimal.valueOf(1000)));
        MenuProduct menuProduct = new MenuProduct(1L, product.getId());
        Menu menu = new Menu("메뉴1", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(menuProduct));
        Menu saved = menuService.create(menu);

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list.get(0).getMenuProducts()).hasSize(saved.getMenuProducts().size());
    }
}
