package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @DisplayName("가격이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void menuWithNullPrice() {
        MenuGroup menuGroup = new MenuGroup("찌개류");

        assertThatThrownBy(() -> new Menu("김치찌개세트", null, menuGroup, List.of(
                new MenuProduct(new Product("김치찌개", BigDecimal.valueOf(1000L)), 1),
                new MenuProduct(new Product("서비스 공기밥", BigDecimal.valueOf(0L)), 1)
        ))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수인 경우 예외가 발생한다.")
    @Test
    void menuWithNegativePrice() {
        MenuGroup menuGroup = new MenuGroup("찌개류");

        assertThatThrownBy(() -> new Menu("김치찌개세트", BigDecimal.valueOf(-1), menuGroup, List.of(
                new MenuProduct(new Product("김치찌개", BigDecimal.valueOf(1000L)), 1),
                new MenuProduct(new Product("서비스 공기밥", BigDecimal.valueOf(0L)), 1)
        ))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 합계보다 가격이 크다면 예외가 발생한다.")
    @Test
    void menuWithPriceMoreThanProductPriceSum() {
        MenuGroup menuGroup = new MenuGroup("찌개류");

        assertThatThrownBy(() -> new Menu("김치찌개세트", BigDecimal.valueOf(2500), menuGroup, List.of(
                new MenuProduct(new Product("김치찌개", BigDecimal.valueOf(1000L)), 1),
                new MenuProduct(new Product("서비스 공기밥", BigDecimal.valueOf(0L)), 1)
        ))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 생성되는 경우를 테스트한다.")
    @Test
    void menuCreate() {
        MenuGroup menuGroup = new MenuGroup("찌개류");

        assertDoesNotThrow(() -> new Menu("김치찌개세트", BigDecimal.valueOf(1000), menuGroup, List.of(
                new MenuProduct(new Product("김치찌개", BigDecimal.valueOf(1000L)), 1),
                new MenuProduct(new Product("서비스 공기밥", BigDecimal.valueOf(0L)), 1)
        )));
    }
}
