package kitchenpos.domain;

import static kitchenpos.exception.MenuExceptionType.SUM_OF_MENU_PRODUCTS_PRICE_MUST_BE_LESS_THAN_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MenuTest {

    @Test
    void 메뉴의_가격이_메뉴_상품_가격들의_합보다_크면_예외가_발생한다() {
        // given
        Price 메뉴가격 = new Price(BigDecimal.valueOf(12));
        Product 가격이_1인_상품 = new Product(null, new Price(BigDecimal.valueOf(1)));
        Product 가격이_3인_상품 = new Product(null, new Price(BigDecimal.valueOf(3)));
        MenuProduct 가격이_1인_상품_2개 = new MenuProduct(가격이_1인_상품, 2);
        MenuProduct 가격이_3인_상품_3개 = new MenuProduct(가격이_3인_상품, 3);

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new Menu(null, 메뉴가격, null, new MenuProducts(List.of(
                        가격이_1인_상품_2개, 가격이_3인_상품_3개
                )))).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(SUM_OF_MENU_PRODUCTS_PRICE_MUST_BE_LESS_THAN_PRICE);
    }

    @ParameterizedTest
    @CsvSource({"11", "10"})
    void 메뉴의_가격이_메뉴_상품_가격들의_합보다_작거나_같으면_예외가_발생하지_않는다(long price) {
        // given
        Price 메뉴가격 = new Price(BigDecimal.valueOf(price));
        Product 가격이_1인_상품 = new Product(null, new Price(BigDecimal.valueOf(1)));
        Product 가격이_3인_상품 = new Product(null, new Price(BigDecimal.valueOf(3)));
        MenuProduct 가격이_1인_상품_2개 = new MenuProduct(가격이_1인_상품, 2);
        MenuProduct 가격이_3인_상품_3개 = new MenuProduct(가격이_3인_상품, 3);

        // when & then
        assertThatCode(() -> new Menu(null, 메뉴가격, null, new MenuProducts(List.of(
                가격이_1인_상품_2개, 가격이_3인_상품_3개
        )))).doesNotThrowAnyException();
    }
}
