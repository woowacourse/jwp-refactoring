package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    @Test
    @DisplayName("메뉴상품이 총 얼마어치인지 메뉴 상품을 구성하는 상품의 가격 총합을 계산한다.")
    void calculateTotalPrice() {
        // given
        final Product 후라이드 = new Product("후라이드", BigDecimal.valueOf(17000));
        final Product 양념치킨 = new Product("양념치킨", BigDecimal.valueOf(20000));
        final MenuProduct 후라이드_1개 = new MenuProduct(후라이드, 1l);
        final MenuProduct 양념치킨_2개 = new MenuProduct(양념치킨, 2l);
        final MenuProducts menuProducts = new MenuProducts(List.of(후라이드_1개, 양념치킨_2개));

        // when
        final Price actual = menuProducts.calculateTotalPrice();

        // then
        assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(57000)));
    }
}
