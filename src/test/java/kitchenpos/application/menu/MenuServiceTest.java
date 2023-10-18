package kitchenpos.application.menu;

import kitchenpos.application.MenuService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
                menuRepository,
                menuGroupRepository,
                productRepository
        );
    }

    @DisplayName("메뉴 가격")
    @Nested
    class MenuPriceNestedTest {

        @DisplayName("[SUCCESS] 등록할 신규 메뉴에 메뉴 상품이 없을 경우 메뉴의 가격은 0원이어야 등록할 수 있다.")
        @Test
        void success_create_menu_when_MenuProductsIsEmpty() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            final Menu expected = new Menu(
                    new Name("테스트용 메뉴명"),
                    new Price("0"),
                    savedMenuGroup,
                    Collections.emptyList()
            );

            // when
            final Menu actual = menuService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getName()).isEqualTo(expected.getName());
                softly.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
                softly.assertThat(actual.getMenuGroup()).isEqualTo(expected.getMenuGroup());
                softly.assertThat(actual.getMenuProducts()).isEqualTo(expected.getMenuProducts());
            });
        }

        @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 메뉴 상품 목록들의 가격 합보다 메뉴의 가격이 높을 경우 예외가 발생한다.")
        @Test
        void throwException_when_create_Menu_IfMenuProductsPriceSum_IsGreaterThanMenuPrice() {
            // given
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            // when
            final Menu expected = new Menu(
                    new Name("테스트용 메뉴명"),
                    new Price("10000"),
                    savedMenuGroup,
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
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            // when
            final Menu expected = new Menu(
                    new Name("테스트용 메뉴명"),
                    new Price("10000"),
                    savedMenuGroup,
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
            final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));

            final List<MenuProduct> menuProducts = new ArrayList<>();
            for (int count = 1; count <= 10; count++) {
                final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), new Price("10000")));
                menuProducts.add(new MenuProduct(null, savedProduct, new Quantity(10)));
            }

            final Menu expected = new Menu(
                    new Name("테스트용 메뉴명"),
                    new Price("0"),
                    savedMenuGroup,
                    new ArrayList<>()
            );
            expected.addMenuProducts(menuProducts);

            // when
            final Menu actual = menuService.create(expected);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isPositive();
                softly.assertThat(actual.getName()).isEqualTo(expected.getName());
                softly.assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
                softly.assertThat(actual.getMenuGroup()).isEqualTo(expected.getMenuGroup());
                softly.assertThat(actual.getMenuProducts())
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(expected.getMenuProducts());
            });
        }
    }
}
