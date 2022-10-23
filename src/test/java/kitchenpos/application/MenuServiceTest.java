package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuService 클래스의")
class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("list 메서드는 모든 메뉴를 조회한다.")
    void list() {
        // given
        Product product = saveProduct("크림치킨", BigDecimal.valueOf(15000.00));
        MenuGroup menuGroup = saveMenuGroup("반마리치킨");
        saveMenu("크림치킨", menuGroup, product);
        saveMenu("크림어니언치킨", menuGroup, product);
        saveMenu("크림치즈치킨", menuGroup, product);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(3);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("메뉴를 생성한다.")
        void create() {
            // given
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(10000.00));
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("맛있는크림치킨", BigDecimal.valueOf(10000.00), menuGroup.getId(), menuProduct);

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            Optional<Menu> actual = menuDao.findById(savedMenu.getId());
            assertThat(actual).isPresent();
        }

        @Test
        @DisplayName("메뉴의 price가 0 미만인 경우 예외를 던진다.")
        void menuPrice_LessThanZero_ExceptionThrown() {
            // given
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(10000.00));
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("맛있는크림치킨", BigDecimal.valueOf(-10000.00), menuGroup.getId(), menuProduct);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("menuGroup이 존재하지 않는 경우 예외를 던진다.")
        void menuGroup_NotExist_ExceptionThrown() {
            // given
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(10000.00));
            MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
            Menu menu = createMenu("맛있는크림치킨", BigDecimal.valueOf(10000.00), 1L, menuProduct);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴의 price가 각 menuProduct의 (상품 가격 * 수량)의 합계보다 큰 경우 예외를 던진다.")
        void price_GreaterThanAmount_ExceptionThrown() {
            // given
            Product product = saveProduct("크림치킨", BigDecimal.valueOf(10000.00));
            MenuGroup menuGroup = saveMenuGroup("반마리치킨");
            MenuProduct menuProduct = createMenuProduct(product.getId(), 2);
            Menu menu = createMenu("맛있는크림치킨", BigDecimal.valueOf(30000.00), menuGroup.getId(), menuProduct);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
