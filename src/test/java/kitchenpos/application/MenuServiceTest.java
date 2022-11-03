package kitchenpos.application;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.공짜_어묵국물;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.support.ServiceTestBase;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTestBase {

    @Test
    void 메뉴_정상_생성() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록(product);

        MenuRequest menuRequest = 떡볶이.toRequest(menuGroup.getId(), menuProducts);

        // when
        MenuResponse response = menuService.create(menuRequest);

        // then
        Optional<Menu> actualMenu = menuDao.findById(response.getId());
        assertThat(actualMenu).isNotEmpty();
    }

    @Test
    void 메뉴의_가격은_0_미만은_메뉴_생성_불가능() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록(product);

        MenuRequest menuRequest = 떡볶이.toRequest(-1L, menuGroup.getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_메뉴_그룹으로_메뉴_생성_불가능() {
        // given
        Product product = productDao.save(불맛_떡볶이.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록(product);

        MenuRequest request = 떡볶이.toRequest(100L, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_합계가_0_이하라면_메뉴_생성_불가능() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(공짜_어묵국물.toEntity());
        List<MenuProduct> menuProducts = 메뉴_상품_목록(product);

        MenuRequest menuRequest = 떡볶이.toRequest(menuGroup.getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격은_상품의_합계보다_작거나_같음() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Product product = productDao.save(불맛_떡볶이.toEntity());

        long quantity = 4;
        List<MenuProduct> menuProducts = 메뉴_상품_목록(quantity, product);
        long menuProductsSum = product.getPrice().longValue() * quantity;

        MenuRequest menuRequest = 떡볶이.toRequest(
                menuProductsSum + 1000,
                menuGroup.getId(),
                menuProducts
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_목록_조회() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(MenuGroupFixture.분식.toEntity());
        Menu menu = 떡볶이.toEntity(menuGroup.getId(), new ArrayList<>());
        menuDao.save(menu);

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        MenuResponse actual = menus.get(0);
        assertThat(actual.getId()).isNotNull();
    }
}
