package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
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

        final List<MenuProductCreateRequest> menuProducts = Arrays.asList(new MenuProductCreateRequest(1L, 1L),
                 new MenuProductCreateRequest(2L, 1L));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("name", BigDecimal.valueOf(2000L), savedMenuGroup.getId(),
                menuProducts);
        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup.getId(), 2000L, savedProduct1, savedProduct2);

        // when
        final Menu actual = menuService.create(menuCreateRequest);

        // then
        assertAll(
                () -> assertThat(actual.getMenuProducts()).extracting("seq")
                        .containsExactlyElementsOf(Arrays.asList(1L, 2L)),
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts")
                        .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                        .isEqualTo(menu)
        );
    }

    @ParameterizedTest
    @NullSource
    @CsvSource(value = {"-1"})
    @DisplayName("메뉴의 가격이 Null이거나 음수 일 수 없다.")
    void create_exceptionWhenPrizeIsNullOrNegative(final BigDecimal price) {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 =serviceDependencies.save(product1);
        final Product savedProduct2 =serviceDependencies.save(product2);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = serviceDependencies.save(menuGroup);

        final List<MenuProductCreateRequest> menuProducts = Arrays.asList(new MenuProductCreateRequest(1L, 1L),
                new MenuProductCreateRequest(2L, 1L));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("name", price, savedMenuGroup.getId(),
                menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
    }


    @Test
    @DisplayName("등록하려는 메뉴의 메뉴 그룹이 등록되어 있어야 한다.")
    void create_exceptionWhenMenuGroupNotExists() {
        // given
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 =serviceDependencies.save(product1);
        final Product savedProduct2 =serviceDependencies.save(product2);

        final MenuGroup notSavedMenuGroup = MenuGroupFixture.createDefaultWithNotSavedId();

        final List<MenuProductCreateRequest> menuProducts = Arrays.asList(new MenuProductCreateRequest(1L, 1L),
                new MenuProductCreateRequest(2L, 1L));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("name", BigDecimal.valueOf(2000L), notSavedMenuGroup.getId(),
                menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    @DisplayName("메뉴의 메뉴 상품들의 상품이 등록되어 있어야 한다.")
    void create_exceptionWhenMenuProductNotExists() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = serviceDependencies.save(menuGroup);

        final List<MenuProductCreateRequest> menuProducts = Arrays.asList(new MenuProductCreateRequest(-1L, 1L),
                new MenuProductCreateRequest(-2L, 1L));
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("name", BigDecimal.valueOf(2000L), savedMenuGroup.getId(),
                menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(menuCreateRequest))
                .isExactlyInstanceOf(ProductNotFoundException.class);
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

        final List<MenuProductCreateRequest> menuProducts = Arrays.asList(new MenuProductCreateRequest(1L, 1L),
                new MenuProductCreateRequest(2L, 1L));
        MenuCreateRequest request = new MenuCreateRequest("name", BigDecimal.valueOf(2001L), savedMenuGroup.getId(),
                menuProducts);

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
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

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup.getId(), 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = serviceDependencies.save(menu);

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual)
                .extracting("id")
                .containsExactly(1L);
    }
}
