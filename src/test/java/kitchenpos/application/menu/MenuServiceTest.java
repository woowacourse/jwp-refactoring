package kitchenpos.application.menu;

import kitchenpos.application.MenuService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuServiceTest extends ApplicationTestConfig {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(
                menuDao,
                menuGroupDao,
                menuProductDao,
                productDao
        );
    }

    @DisplayName("메뉴 가격")
    @Nested
    class MenuPriceNestedTest {

        @DisplayName("[SUCCESS] 등록할 신규 메뉴에 메뉴 상품이 없을 경우 메뉴의 가격은 0원이어야 등록할 수 있다.")
        @Test
        void success_create_menu_when_MenuProductsIsEmpty() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));

            final BigDecimal zeroPrice = BigDecimal.ZERO;
            final Menu expected = new Menu(
                    "테스트용 메뉴명",
                    zeroPrice,
                    savedMenuGroup.getId(),
                    Collections.emptyList()
            );

            // when
            final Menu actual = menuService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getName()).isEqualTo(expected.getName());
                softly.assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
                softly.assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
                softly.assertThat(actual.getMenuProducts()).isEqualTo(expected.getMenuProducts());
            });
        }

        @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 메뉴 상품 목록들의 가격 합보다 메뉴의 가격이 높을 경우 예외가 발생한다.")
        @Test
        void throwException_when_create_Menu_IfMenuProductsPriceSum_IsGreaterThanMenuPrice() {
            // given
            final MenuGroup unsavedMenuGroup = new MenuGroup("테스트용 메뉴 그룹명");

            // when
            final Menu expected = new Menu(
                    "테스트용 메뉴명",
                    new BigDecimal("10000"),
                    unsavedMenuGroup.getId(),
                    Collections.emptyList()
            );

            // then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 그룹")
    @Nested
    class MenuGroupNestedTest {

        @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
        @Test
        void throwException_when_create_Menu_IfMenuGroupIsNotExists() {
            // given
            final MenuGroup unsavedMenuGroup = new MenuGroup("테스트용 메뉴 그룹명");

            // when
            final Menu expected = new Menu(
                    "테스트용 메뉴명",
                    new BigDecimal("10000"),
                    unsavedMenuGroup.getId(),
                    Collections.emptyList()
            );

            // then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 상품")
    @Nested
    class MenuProductNestedTest {

        @DisplayName("[SUCCESS] 등록할 신규 메뉴에 메뉴 상품 목록을 같이 등록할 수 있다.")
        @Test
        void success_create_menu_with_MenuProducts() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));

            final Long menuIdIsNotProblem = null;
            final List<MenuProduct> menuProducts = new ArrayList<>();
            for (int count = 1; count <= 10; count++) {
                final Product savedProduct = productDao.save(new Product("테스트용 상품명", new BigDecimal("10000")));
                menuProducts.add(new MenuProduct(menuIdIsNotProblem, savedProduct.getId(), 10));
            }

            final Menu expected = new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    menuProducts
            );

            // when
            final Menu actual = menuService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getName()).isEqualTo(expected.getName());
                softly.assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
                softly.assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
                softly.assertThat(actual.getMenuProducts())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(expected.getMenuProducts());
            });
        }

        @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 메뉴 상품 목록 중 상품이 등록되어 있지 않으면 예외가 발생한다")
        @Test
        void throwException_when_create_Menu_IfMenuProduct_ProductIsNotExists() {
            // given
            final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));

            final Long menuIdIsNotProblem = null;
            final List<MenuProduct> unsavedMenuProducts = List.of(
                    new MenuProduct(menuIdIsNotProblem, 1L, 10),
                    new MenuProduct(menuIdIsNotProblem, 2L, 10),
                    new MenuProduct(menuIdIsNotProblem, 3L, 10),
                    new MenuProduct(menuIdIsNotProblem, 4L, 10),
                    new MenuProduct(menuIdIsNotProblem, 5L, 10)
            );

            final Menu expected = new Menu(
                    "테스트용 메뉴명",
                    BigDecimal.ZERO,
                    savedMenuGroup.getId(),
                    unsavedMenuProducts
            );

            // then
            assertThatThrownBy(() -> menuService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
