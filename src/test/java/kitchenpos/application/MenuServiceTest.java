package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    void 메뉴를_생성할_수_있다() {
        // given
        Menu menu = 후라이드후라이드(BigDecimal.valueOf(19000));

        // when
        Menu actual = menuService.create(menu);

        // then
        menu.getMenuProducts().forEach(menuProduct -> menuProduct.setMenuId(actual.getId()));
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(menu.getName()),
                () -> assertThat(actual.getPrice()).isCloseTo(menu.getPrice(), Percentage.withPercentage(0)),
                () -> assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(actual.getMenuProducts()).extracting("menuId", "productId", "quantity")
                        .containsExactly(tuple(actual.getId(), menu.getMenuProducts().get(0).getProductId(),
                                menu.getMenuProducts().get(0).getQuantity()))
        );
    }

    @Test
    void 메뉴_가격이_null인_경우_메뉴를_생성할_수_없다() {
        // given
        Menu menu = 후라이드후라이드(null);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0보다_작은_경우_메뉴를_생성할_수_없다() {
        // given
        Menu menu = 후라이드후라이드(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않는_경우_메뉴를_생성할_수_없다() {
        // given
        Menu menu = 후라이드후라이드(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(-1L);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_상품_개별_가격의_총합보다_크다면_메뉴를_생성할_수_없다() {
        // given
        Menu menu = 후라이드후라이드(BigDecimal.valueOf(20001));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Menu menu = 후라이드후라이드(BigDecimal.valueOf(19000));
        menuService.create(menu);

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    public Menu 후라이드후라이드(BigDecimal price) {
        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(price);
        menu.setMenuGroupId(추천메뉴().getId());
        menu.setMenuProducts(Collections.singletonList(후라이드후라이드_메뉴_상품()));

        return menu;
    }

    public MenuGroup 추천메뉴() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        return menuGroupDao.save(menuGroup);
    }

    public MenuProduct 후라이드후라이드_메뉴_상품() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(후라이드().getId());
        menuProduct.setQuantity(2);

        return menuProduct;
    }


    public Product 후라이드() {
        Product product = new Product();
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(10000));
        return productDao.save(product);
    }
}
