package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Menu 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    private static Stream<Arguments> create() {
        return Stream.of(
                Arguments.of(MenuFixture.후라이드치킨_NO_KEY.getName(), MenuFixture.후라이드치킨_NO_KEY.getPrice(),
                        MenuFixture.후라이드치킨_NO_KEY.getMenuGroupId(), MenuFixture.후라이드치킨_NO_KEY.getMenuProducts()),
                Arguments.of(MenuFixture.후라이드치킨_NO_KEY.getName(), MenuFixture.후라이드치킨_NO_KEY.getPrice(),
                        MenuFixture.후라이드치킨_NO_KEY.getMenuGroupId(), MenuFixture.후라이드치킨_NO_KEY.getMenuProducts())
        );
    }

    @DisplayName("메뉴 저장 - 성공")
    @CustomParameterizedTest
    @MethodSource("create")
    void create(@AggregateWith(MenuAggregator.class) Menu menu) {
        //given
        //when
        final Menu actual = menuService.create(menu);
        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(menu.getName());
        assertThat(actual.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴의 가격이 null")
    @Test
    void createFailureWhenPriceNull() {
        //given
        final Menu menu = new Menu();
        menu.setName(MenuFixture.후라이드치킨_NO_KEY.getName());
        menu.setPrice(null);
        menu.setMenuGroupId(MenuFixture.후라이드치킨_NO_KEY.getMenuGroupId());
        menu.setMenuProducts(MenuFixture.후라이드치킨_NO_KEY.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴의 가격이 음수")
    @Test
    void createFailureWhenPriceMinus() {
        //given
        final Menu menu = new Menu();
        menu.setName(MenuFixture.후라이드치킨_NO_KEY.getName());
        menu.setPrice(new BigDecimal("-1"));
        menu.setMenuGroupId(MenuFixture.후라이드치킨_NO_KEY.getMenuGroupId());
        menu.setMenuProducts(MenuFixture.후라이드치킨_NO_KEY.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴그룹이 존재하지 않음")
    @CustomParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void createFailureWhenNotFoundMenuGroup(Long menuGroupId) {
        //given
        final Menu menu = new Menu();
        menu.setName(MenuFixture.후라이드치킨_NO_KEY.getName());
        menu.setPrice(MenuFixture.후라이드치킨_NO_KEY.getPrice());
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(MenuFixture.후라이드치킨_NO_KEY.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴상품의 상품이 존재하지 않음")
    @CustomParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void createFailureWhenNotFoundMenuGroupProduct(Long productId) {
        //given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY.getMenuId());
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(MenuProductFixture.후라이드치킨_후라이드치킨_NO_KEY.getQuantity());
        final Menu menu = new Menu();
        menu.setName(MenuFixture.후라이드치킨_NO_KEY.getName());
        menu.setPrice(MenuFixture.후라이드치킨_NO_KEY.getPrice());
        menu.setMenuGroupId(MenuFixture.후라이드치킨_NO_KEY.getMenuGroupId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 저장하려는 메뉴의 가격이 상품의 가격*수량 보다 높음")
    @Test
    void createFailureWhenMenuPriceOverThanProducePriceMultiQuantity() {
        //given
        final Menu menu = new Menu();
        menu.setName(MenuFixture.후라이드치킨_NO_KEY.getName());
        menu.setPrice(ProductFixture.후라이드치킨.getPrice()
                .multiply(new BigDecimal(MenuProductFixture.후라이드후라이드_후라이드치킨_NO_KEY.getQuantity()))
                .add(BigDecimal.ONE)
        );
        menu.setMenuGroupId(MenuFixture.후라이드치킨_NO_KEY.getMenuGroupId());
        menu.setMenuProducts(MenuFixture.후라이드치킨_NO_KEY.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 조회 - 성공 - 전체 메뉴 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<Menu> actual = menuService.list();
        //then
        assertThat(actual).extracting(Menu::getName)
                .containsAnyElementsOf(MenuFixture.menusName());
    }

    private static class MenuAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            final Menu menu = new Menu();
            menu.setName(accessor.getString(0));
            menu.setPrice(accessor.get(1, BigDecimal.class));
            menu.setMenuGroupId(accessor.getLong(2));
            menu.setMenuProducts(accessor.get(3, List.class));
            return menu;
        }
    }
}