package kitchenpos.menu.appllication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.test.fixtures.MenuFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    MenuService menuService;

    @Nested
    @DisplayName("메뉴를 등록할 때")
    class CreateMenu {
        @Test
        @DisplayName("정상 등록된다.")
        void createMenu() {
            // given
            final Menu menu = MenuFixtures.BASIC.get();

            // when
            final Menu saved = menuService.create(menu);

            // then
            assertSoftly(softly -> {
                softly.assertThat(saved.getName()).isEqualTo(menu.getName());
                softly.assertThat(saved.getPrice().intValue()).isEqualTo(menu.getPrice().intValue());
                softly.assertThat(saved.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            });
        }

        @ParameterizedTest(name = "price = {0}")
        @ValueSource(ints = {-1, 999999})
        @DisplayName("메뉴의 가격이 올바르지 않을 시 예외 발생")
        void menuPriceLessThanZeroWonException(final int price) {
            // given
            final Menu menu = MenuFixtures.BASIC.get();
            menu.setPrice(new BigDecimal(price));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menu));
        }

        @Test
        @DisplayName("메뉴의 가격이 null일 시 예외 발생")
        void menuPriceNullException() {
            // given
            final Menu menu = MenuFixtures.BASIC.get();
            menu.setPrice(null);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menu));
        }

        @Test
        @DisplayName("메뉴 그룹이 존재하지 않을 시 예외 발생")
        void menuGroupNotExistException() {
            // given
            final Menu menu = MenuFixtures.BASIC.get();
            menu.setMenuGroupId(-1L);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menu));
        }

        @Test
        @DisplayName("메뉴 상품이 존재하지 않을 시 예외 발생")
        void menuProductsNotExistException() {
            // given
            final Menu menu = MenuFixtures.BASIC.get();
            final List<MenuProduct> menuProducts = menu.getMenuProducts();
            menuProducts.get(0).setProductId(-1L);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> menuService.create(menu));
        }
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다")
    void getMenus() {
        // given
        menuService.create(MenuFixtures.BASIC.get());

        // when
        final List<Menu> actualMenus = menuService.list();

        // then
        assertThat(actualMenus).isNotEmpty();
    }
}
