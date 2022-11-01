package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Menu 클래스의")
class MenuTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1", "-1000"})
        @DisplayName("메뉴의 가격이 null이거나 음수인 경우 예외를 던진다.")
        void price_LessThanZero_ExceptionThrown(final BigDecimal price) {
            assertThatThrownBy(() -> new Menu("크림치킨", Price.valueOf(price), 1L, List.of(new MenuProduct(1L, 1L, BigDecimal.TEN))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 상품의 (수량 * 금액) 보다 메뉴의 가격이 높은 경우 예외를 던진다.")
        void price_MoreThanSumOfMenuProducts_ExceptionThrown() {
            assertThatThrownBy(
                    () -> new Menu("크림치킨", Price.valueOf(BigDecimal.valueOf(100L)), 1L,
                            List.of(new MenuProduct(1L, 1L, BigDecimal.TEN))))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("updateMenuIdOfMenuProducts 메서드는")
    class UpdateMenuIdOfMenuProducts {

        @Test
        @DisplayName("메뉴 상품들의 메뉴 id를 업데이트한다.")
        void success() {
            final Menu menu = new Menu(1L, "크림치킨", Price.valueOf(BigDecimal.valueOf(30L)), 1L,
                    List.of(new MenuProduct(1L, 1L, BigDecimal.TEN), new MenuProduct(1L, 2L, BigDecimal.TEN)));
            menu.updateMenuIdOfMenuProducts();

            final Set<Long> menuIds = menu.getMenuProducts()
                    .stream()
                    .map(MenuProduct::getMenuId)
                    .collect(Collectors.toSet());
            assertAll(
                    () -> assertThat(menuIds).hasSize(1),
                    () -> assertThat(menuIds).containsExactly(1L)
            );
        }
    }
}
