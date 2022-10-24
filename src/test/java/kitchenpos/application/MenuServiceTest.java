package kitchenpos.application;

import static kitchenpos.DomainFixtures.라면_메뉴;
import static kitchenpos.DomainFixtures.맛있는_라면;
import static kitchenpos.DomainFixtures.면_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Test
    void 메뉴를_성공적으로_추가할_수_있다() {
        Product 라면 = productService.create(맛있는_라면());
        MenuGroup 면 = menuGroupService.create(면_메뉴_그룹());

        Menu 라면_메뉴 = 라면_메뉴();
        라면_메뉴.setMenuGroupId(면.getId());
        라면_메뉴.setMenuProducts(메뉴_상품(라면));
        Menu ramen = menuService.create(라면_메뉴);

        assertThat(ramen.getName()).isEqualTo("라면");
    }

    @Test
    void 메뉴의_가격이_null이거나_0보다_작으면_예외가_발생한다() {
        MenuGroup 면 = menuGroupService.create(new MenuGroup("면"));
        Menu 라면_메뉴 = new Menu("라면", new BigDecimal(-1), 면.getId());

        assertThatThrownBy(
                () -> menuService.create(라면_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        Menu 라면_메뉴 = 라면_메뉴();
        라면_메뉴.setMenuGroupId(1L);

        assertThatThrownBy(
                () -> menuService.create(라면_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품을_사용하면_예외가_발생한다() {
        Menu 라면_메뉴 = 라면_메뉴();

        assertThatThrownBy(
                () -> menuService.create(라면_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_상품들의_가격의_합보다_크면_예외가_발생한다() {
        Product 라면 = productService.create(맛있는_라면());
        MenuGroup 면 = menuGroupService.create(면_메뉴_그룹());

        Menu 라면_메뉴 = 라면_메뉴();
        라면_메뉴.setPrice(new BigDecimal(1500));
        라면_메뉴.setMenuGroupId(면.getId());
        라면_메뉴.addMenuProduct(new MenuProduct(null, 라면.getId(), 1));

        assertThatThrownBy(
                () -> menuService.create(라면_메뉴)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴를 성공적으로 조회할 수 있다.")
    void 메뉴를_성공적으로_조회할_수_있다() {
        Product 라면 = productService.create(맛있는_라면());
        List<MenuProduct> 메뉴_상품들 = 메뉴_상품(라면);
        메뉴_상품들.add(new MenuProduct(null, 라면.getId(), 1));

        MenuGroup menuGroup = menuGroupService.create(면_메뉴_그룹());
        Menu ramen = menuService.create(new Menu("라면", new BigDecimal(1200), menuGroup.getId(), 메뉴_상품들));
        Menu friedRice = menuService.create(new Menu("짜파게티", new BigDecimal(1300), menuGroup.getId(), 메뉴_상품들));
        메뉴_상품들.get(0).setMenuId(ramen.getId());
        메뉴_상품들.get(1).setMenuId(friedRice.getId());

        List<Menu> menus = menuService.list();

        assertAll(
                () -> assertThat(menus.size()).isEqualTo(2),
                () -> assertThat(menus.get(0).getName()).isEqualTo("라면"),
                () -> assertThat(menus.get(1).getName()).isEqualTo("짜파게티")
        );
    }

    private List<MenuProduct> 메뉴_상품(Product product) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(null, product.getId(), 1));
        return menuProducts;
    }
}
