package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuProductHistoryTest {

    @DisplayName("[SUCCESS] 메뉴 상품을 통해 메뉴 상품 기록을 생성한다.")
    @Test
    void success_create() {
        // given
        final Product product = new Product(new Name("테스트용 상품명"), Price.from("10000"));
        final MenuProduct menuProduct = new MenuProduct(product, new Quantity(10));

        // when
        final MenuProductHistory actual = MenuProductHistory.from(menuProduct);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isNull();
            softly.assertThat(actual.getName()).isEqualTo(product.getName());
            softly.assertThat(actual.getPrice()).isEqualTo(product.getPrice());
            softly.assertThat(actual.getQuantity()).isEqualTo(menuProduct.getQuantity());
        });
    }
}
