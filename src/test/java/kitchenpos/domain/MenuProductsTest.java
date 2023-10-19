package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuProductsTest {

    @DisplayName("[SUCCESS] 비어 있는 메뉴 상품 목록을 생성한다.")
    @Test
    void success_empty() {
        // given
        final MenuProducts actual = MenuProducts.empty();

        // expect
        assertThat(actual.getMenuProductItems()).isEmpty();
    }

    @DisplayName("[SUCCESS] 메뉴 상품을 메뉴에 주입할 때 메뉴 상품에 현재 메뉴를 추가한다.")
    @Test
    void success_add() {
        // given
        final Menu menu = new Menu(
                new Name("테스트용 메뉴명"),
                Price.ZERO,
                new MenuGroup(new Name("테스트용 메뉴 그룹명")),
                MenuProducts.empty()
        );

        final Product product = new Product(new Name("테스트용 상품명"), new Price("10000"));
        final Quantity quantity = new Quantity(10);

        // when
        final MenuProducts actual = MenuProducts.empty();
        actual.add(menu, List.of(
                MenuProduct.ofWithoutMenu(product, quantity)
        ));

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getMenuProductItems()).hasSize(1);
            final MenuProduct actualMenuProduct = actual.getMenuProductItems().get(0);

            softly.assertThat(actualMenuProduct.getMenu()).isEqualTo(menu);
            softly.assertThat(actualMenuProduct.getProduct()).isEqualTo(product);
            softly.assertThat(actualMenuProduct.getQuantity()).isEqualTo(quantity);
        });
    }

    @DisplayName("[SUCCESS] 메뉴 상품 가격 목록의 총합을 계산한다.")
    @Test
    void success_getTotalPrice() {
        // given
        final MenuProducts menuProducts = MenuProducts.empty();
        menuProducts.add(null, List.of(
                MenuProduct.ofWithoutMenu(new Product(new Name("테스트용 상품명"), new Price("10000")), new Quantity(1)),
                MenuProduct.ofWithoutMenu(new Product(new Name("테스트용 상품명"), new Price("5000")), new Quantity(2)),
                MenuProduct.ofWithoutMenu(new Product(new Name("테스트용 상품명"), new Price("2000")), new Quantity(5))
        ));

        // when
        final Price actual = menuProducts.getTotalPrice();

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(new BigDecimal("30000"));
    }
}
