package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidMenuProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProduct 단위 테스트")
class MenuProductTest {

    @DisplayName("MenuProduct가 생성될 때")
    @Nested
    class Create {

        @DisplayName("Menu가 null이면 예외가 발생한다.")
        @Test
        void menuNullException() {
            // given
            Product product = new Product("스테커2 버거", BigDecimal.valueOf(11_900));

            // when, then
            assertThatCode(() -> new MenuProduct(null, product, 1L))
                .isExactlyInstanceOf(InvalidMenuProductException.class);
        }

        @DisplayName("Product가 null이면 예외가 발생한다.")
        @Test
        void productNullException() {
            // given
            MenuGroup menuGroup = new MenuGroup("버거킹 단품 메뉴 그룹");
            Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(11_900), menuGroup);

            // when, then
            assertThatCode(() -> new MenuProduct(menu, null, 1L))
                .isExactlyInstanceOf(InvalidMenuProductException.class);
        }

        @DisplayName("Quantity가 null이면 예외가 발생한다.")
        @Test
        void quantityNullException() {
            // given
            MenuGroup menuGroup = new MenuGroup("버거킹 단품 메뉴 그룹");
            Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(11_900), menuGroup);
            Product product = new Product("스테커2 버거", BigDecimal.valueOf(11_900));

            // when, then
            assertThatCode(() -> new MenuProduct(menu, product, null))
                .isExactlyInstanceOf(InvalidMenuProductException.class);
        }

        @DisplayName("Quantity가 음수면 예외가 발생한다.")
        @Test
        void quantityNegativeException() {
            // given
            MenuGroup menuGroup = new MenuGroup("버거킹 단품 메뉴 그룹");
            Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(11_900), menuGroup);
            Product product = new Product("스테커2 버거", BigDecimal.valueOf(11_900));

            // when, then
            assertThatCode(() -> new MenuProduct(menu, product, -1L))
                .isExactlyInstanceOf(InvalidMenuProductException.class);
        }
    }

    @DisplayName("상품의 총 금액은 quantity가 곱해진 값이 반환된다.")
    @Test
    void totalPrice() {
        // given
        MenuGroup menuGroup = new MenuGroup("버거킹 단품 메뉴 그룹");
        Menu menu = new Menu("스테커2 버거 단품 메뉴", BigDecimal.valueOf(20_000), menuGroup);
        Product product = new Product("좋은 상품2", BigDecimal.valueOf(5_000));
        MenuProduct menuProduct = new MenuProduct(menu, product, 4L);

        // when
        Price totalPrice = menuProduct.productTotalPrice();

        // then
        assertThat(totalPrice.getValue()).isEqualTo(BigDecimal.valueOf(20_000));
    }
}