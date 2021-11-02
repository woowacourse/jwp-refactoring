package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.IntegrationTest;
import kitchenpos.common.fixture.TMenu;
import kitchenpos.common.fixture.TMenuGroup;
import kitchenpos.common.fixture.TMenuProduct;
import kitchenpos.common.fixture.TProduct;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import net.bytebuddy.implementation.MethodCall.ArgumentLoader.ArgumentProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsSources;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuServiceTest extends IntegrationTest {

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        Menu menu = menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        assertThat(menu)
            .usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes(".*seq", ".*id", ".*price")
            .isEqualTo(TMenu.builder()
                .name("후라이드+후라이드")
                .menuGroupId(recommendMenu.getId())
                .menuProducts(Collections.singletonList(menuProduct))
                .build()
            );

        assertThat(menu.getPrice().intValue()).isEqualTo(19000);
    }

    @DisplayName("메뉴 이름은 중복을 허용한다")
    @Test
    void create_allowDuplicate() {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        assertThatCode(() -> menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build())
        ).doesNotThrowAnyException();
    }

    @DisplayName("메뉴의 가격은 null이거나 음수일 수 없다.")
    @ParameterizedTest
    @MethodSource("getParametersForPriceNotBeNullOrNegative")
    void create_priceNotBeNullOrNegative(BigDecimal price) {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        assertThatThrownBy(() -> menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(price)
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getParametersForPriceNotBeNullOrNegative() {
        return Stream.of(
            Arguments.of(BigDecimal.valueOf(-19000)),
            null
        );
    }

    @DisplayName("메뉴는 반드시 특정 메뉴그룹에 속해야 한다.")
    @Test
    void create_menuMustBelongToGroup() {
        assertThatThrownBy(() -> menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuProducts(Collections.emptyList())
            .build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 상품 가격의 총 합보다 작거나 같아야 한다.")
    @Test
    void create_menuPriceMustLessThenOrEqualToTheSumOfTheProductPrices() {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        assertThatThrownBy(() -> menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(400000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 목록을 불러올 수 있다.")
    @Test
    void list() {
        MenuGroup recommendMenu = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        Product friedChicken = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(friedChicken.getId())
            .quantity(2L)
            .builder();

        menuService.create(TMenu.builder()
            .name("후라이드+후라이드1")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        menuService.create(TMenu.builder()
            .name("후라이드+후라이드2")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(recommendMenu.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        assertThat(menuService.list()).hasSize(2);
    }
}
