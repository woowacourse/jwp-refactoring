package kitchenpos.core.domain.menu;

import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.core.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    @DisplayName("메뉴에는 이름이 있어야 한다.")
    void 메뉴_생성_실패_이름_없음() {
        // given
        final String nullName = null;

        // expected
        assertThatThrownBy(() -> new Menu(
                nullName,
                BigDecimal.ZERO,
                1L,
                List.of(new MenuProduct(new Product("상품", ONE), 2))
        )).isInstanceOf(Exception.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 256})
    @DisplayName("메뉴 이름의 길이는 최소 1자, 최대 255자이다.")
    void 메뉴_생성_실패_이름_길이(final int nameLength) {
        // given
        final String name = "a".repeat(nameLength);

        // expected
        assertThatThrownBy(() -> new Menu(
                name,
                BigDecimal.ZERO,
                1L,
                List.of(new MenuProduct(new Product("상품", ONE), 2))
        )).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("메뉴에는 가격이 있어야 한다.")
    void 메뉴_생성_실패_가격_없음() {
        // given
        final BigDecimal nullPrice = null;

        // expected
        assertThatThrownBy(() -> new Menu(
                "순두부 정식",
                nullPrice,
                1L,
                List.of(new MenuProduct(new Product("상품", ONE), 2))
        )).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    void 메뉴_생성_실패_가격_음수() {
        assertThatThrownBy(() -> new Menu(
                "순두부 정식",
                BigDecimal.valueOf(-1),
                1L,
                List.of(new MenuProduct(new Product("상품", ONE), 2))
        )).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("메뉴 가격은 메뉴에 포함된 상품 총액보다 클 수 없다.")
    void 메뉴_생성_실패_상품_총액보다_큰_가격() {
        // given
        final BigDecimal productPrice1 = BigDecimal.valueOf(14000);
        final BigDecimal productPrice2 = BigDecimal.valueOf(8000);
        final Product product1 = new Product("떡갈비", productPrice1);
        final Product product2 = new Product("순두부찌개", productPrice2);
        final BigDecimal product2Quantity = BigDecimal.valueOf(2);

        // when
        final BigDecimal menuPrice = productPrice2.multiply(product2Quantity)
                .add(productPrice1)
                .add(ONE);

        // then
        assertThatThrownBy(() -> new Menu(
                "순두부 2인 정식",
                menuPrice,
                1L,
                List.of(new MenuProduct(product1, 1), new MenuProduct(product2, 2))
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
