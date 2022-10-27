package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTestEnvironment {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 =serviceDependencies.save(product1);
        final Product savedProduct2 =serviceDependencies.save(product2);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = serviceDependencies.save(menuGroup);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup, 2000L, savedProduct1, savedProduct2);

        // when
        final Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual.getMenuProducts())
                        .usingElementComparatorIgnoringFields("seq")
                        .containsExactlyElementsOf(menu.getMenuProducts()),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts")
                        .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                        .isEqualTo(menu)
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-1})
    @DisplayName("메뉴의 가격이 Null이거나 음수 일 수 없다.")
    void create_exceptionWhenPrizeIsNullOrNegative(final Long price) {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 =serviceDependencies.save(product1);
        final Product savedProduct2 =serviceDependencies.save(product2);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = serviceDependencies.save(menuGroup);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup, price, savedProduct1, savedProduct2);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("등록하려는 메뉴의 메뉴 그룹이 등록되어 있어야 한다.")
    void create_exceptionWhenMenuGroupNotExists() {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 =serviceDependencies.save(product1);
        final Product savedProduct2 =serviceDependencies.save(product2);

        final MenuGroup notSavedMenuGroup = MenuGroupFixture.createDefaultWithoutId();

        final Menu menu = MenuFixture.createWithPrice(notSavedMenuGroup, 1000L, savedProduct1, savedProduct2);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 메뉴 상품들의 상품이 등록되어 있어야 한다.")
    void create_exceptionWhenMenuProductNotExists() {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();

        final Menu menu = MenuFixture.createWithPrice(menuGroup, 1000L, product1, product2);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격 합보다 크면 안된다.")
    void create_exceptionWhenMenuProductPrizeSumIsBiggerThanMenuPrize() {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = serviceDependencies.save(menuGroup);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup, 2001L, savedProduct1, savedProduct2);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = serviceDependencies.save(menuGroup);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = serviceDependencies.save(menu);

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual)
                .usingElementComparatorIgnoringFields("menuProducts")
                .containsExactly(savedMenu);
    }
}
