package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴_상품을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        MenuProduct menuProduct = 메뉴_상품을_생성한다(null, productId, 1);
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of(menuProduct));

        Menu savedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isZero(),
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "price", "menuProducts")
                        .isEqualTo(savedMenu),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
                        .usingElementComparatorIgnoringFields("seq")
                        .containsOnly(menuProduct)
        );
    }

    @Test
    void 메뉴_가격이_0원_미만이면_예외를_반환한다() {
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.valueOf(-1), null, null);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_반환한다() {
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.ZERO, 0L, null);

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_상품이_메뉴에_포함되어_있으면_예외를_반환한다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        MenuProduct menuProduct = 메뉴_상품을_생성한다(null, 0L, 1);
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.valueOf(2_000), menuGroupId, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_크면_예외를_반환한다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        MenuProduct menuProduct = 메뉴_상품을_생성한다(null, productId, 1);
        Menu menu = 메뉴를_생성한다("메뉴", BigDecimal.valueOf(2_000), menuGroupId, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴를_조회할_수_있다() {
        Long menuGroupId = menuGroupDao.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long productId = productDao.save(상품을_생성한다("상품", BigDecimal.valueOf(1_000)))
                .getId();
        MenuProduct menuProduct = 메뉴_상품을_생성한다(null, productId, 1);
        Menu menu1 = menuService.create(
                메뉴를_생성한다("메뉴1", BigDecimal.ZERO, menuGroupId, List.of(menuProduct)));
        Menu menu2 = menuService.create(
                메뉴를_생성한다("메뉴2", BigDecimal.ZERO, menuGroupId, List.of(menuProduct)));

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(2)
                .usingElementComparatorIgnoringFields("price", "menuProducts")
                .containsExactly(menu1, menu2);
    }
}
