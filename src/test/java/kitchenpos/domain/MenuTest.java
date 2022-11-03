package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @DisplayName("가격이 0보다 작으면 예외를 발생시킨다.")
    @Test
    void priceLessThanZero_exception() {
        // given
        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(null, new Product("상품1", 2000L), 2),
                new MenuProduct(null, new Product("상품2", 1000L), 2)
        );

        assertThatThrownBy(() -> new Menu("상품1", new Price(-1L), new MenuGroup("메뉴 그룹"), menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품들의 (가격 * 개수) 합보다 메뉴의 가격이 크면 예외를 발생시킨다.")
    @Test
    void priceMoreThanSumOfProducts_exception() {
        // given
        List<MenuProduct> menuProducts = List.of(
                new MenuProduct(null, new Product("상품1", 2000L), 2),
                new MenuProduct(null, new Product("상품2", 1000L), 2)
        );

        assertThatThrownBy(() -> new Menu("상품1", new Price(7000L), new MenuGroup("메뉴 그룹"), menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
