package kitchenpos.application;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.공짜_어묵국물;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTestBase {

    @Test
    void 메뉴_정상_생성() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(product);

        Menu menu = 떡볶이.toEntity(menuGroup.getId(), menuProducts);

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        Optional<Menu> actualMenu = menuDao.findById(savedMenu.getId());
        assertThat(actualMenu).isNotEmpty();
    }

    @Test
    void 메뉴의_가격은_0_미만은_메뉴_생성_불가능() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(product);

        Menu menu = 떡볶이.toEntity(BigDecimal.valueOf(-1), menuGroup.getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴_그룹으로_메뉴_생성_불가능() {
        // given
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(product);

        Menu menu = 떡볶이.toEntity(100L, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_합계가_0_이하라면_메뉴_생성_불가능() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(공짜_어묵국물.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(product);

        Menu menu = 떡볶이.toEntity(menuGroup.getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격은_상품의_합계보다_작거나_같음() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());

        long quantity = 4;
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(quantity, product);
        BigDecimal menuProductsSum = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        Menu menu = 떡볶이.toEntity(
                menuProductsSum.add(BigDecimal.valueOf(1000)),
                menuGroup.getId(),
                menuProducts
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록_조회() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록_생성(product);

        Menu menu = 떡볶이.toEntity(menuGroup.getId(), menuProducts);
        menuService.create(menu);

        // when
        List<Menu> menus = menuService.list();

        // then
        Menu actual = menus.get(0);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMenuProducts())
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(menuProducts);
    }
}
