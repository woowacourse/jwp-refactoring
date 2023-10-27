package kitchenpos.menu.domain;

import kitchenpos.common.vo.Name;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuProductTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴그룹명"));

        assertThatCode(() -> new MenuProduct(product, new Quantity(10)))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 메뉴 없이 메뉴 상품 생성을 성공한다.")
    @Test
    void success_ofWithoutMenu() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final Quantity quantity = new Quantity(10);

        // when
        final MenuProduct actual = MenuProduct.withoutMenu(product, quantity);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getProduct()).isEqualTo(product);
            softly.assertThat(actual.getQuantity()).isEqualTo(quantity);
        });
    }

    @DisplayName("[SUCCESS] 상품과 수를 곱한 가격을 계산한다.")
    @Test
    void success_getTotalPrice() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final Quantity quantity = new Quantity(10);

        // when
        final MenuProduct menuProduct = MenuProduct.withoutMenu(product, quantity);
        final Price actual = menuProduct.getTotalPrice();

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(new BigDecimal("100000"));
    }
}
