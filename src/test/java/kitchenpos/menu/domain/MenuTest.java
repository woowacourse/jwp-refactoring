package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuTest {

    @DisplayName("[SUCCESS] 메뉴 상품 목록을 비어 있는 상태로 메뉴를 생성한다.")
    @Test
    void success_ofEmptyMenuProducts() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));

        // when
        final Menu actual = Menu.withEmptyMenuProducts(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getMenuGroup()).isEqualTo(menuGroup);
            softly.assertThat(actual.getMenuProducts())
                    .usingRecursiveComparison()
                    .isEqualTo(MenuProducts.empty());
        });
    }

    @DisplayName("[EXCEPTION] 메뉴 가격이 null 일 경우 예외가 발생한다.")
    @Test
    void throwException_price_isNull() {
        // expect
        assertThatThrownBy(() -> new Menu(new Name("테스트용 메뉴명"), null, null, MenuProducts.empty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[EXCEPTION] 등록할 신규 메뉴에 가격이 음수일 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "-10", "-100", "-1000000"})
    void throwException_price_isNegative(final String negativePriceValue) {
        // expect
        assertThatThrownBy(() -> new Menu(new Name("테스트용 메뉴명"), Price.from(negativePriceValue), null, MenuProducts.empty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[SUCCESS] 메뉴 상품을 추가할 경우 메뉴 상품에 자신을 추가한다.")
    @Test
    void success_addMenuProducts() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), Price.ZERO, menuGroup, MenuProducts.empty());

        // when
        menu.addMenuProducts(List.of(MenuProduct.withoutMenu(product, new Quantity(10))));

        // then
        final List<MenuProduct> actual = menu.getMenuProducts().getMenuProductItems();
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final MenuProduct actualMenuProduct = actual.get(0);

            softly.assertThat(actualMenuProduct)
                    .usingRecursiveComparison()
                    .isEqualTo(new MenuProduct(product, new Quantity(10)));
        });
    }
}
