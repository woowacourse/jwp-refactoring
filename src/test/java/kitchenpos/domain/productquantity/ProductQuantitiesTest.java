package kitchenpos.domain.productquantity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.price.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductQuantities 도메인 단위테스트")
class ProductQuantitiesTest {

    @DisplayName("price 검사 - 성공 - PriceQuantity들의 price 총 합 >= otherPrice")
    @Test
    void validatePrice_Success() {
        // given
        final int priceValue = 6_000;
        final ProductQuantities productQuantities = 상품이_한_개만_존재하는_ProductQuantities를_생성한다(priceValue);

        // when
        // then
        assertThatCode(() ->
            productQuantities.validateTotalPriceIsGreaterOrEqualThan(new Price(priceValue))
        ).doesNotThrowAnyException();

        assertThatCode(() ->
            productQuantities.validateTotalPriceIsGreaterOrEqualThan(new Price(priceValue - 1))
        ).doesNotThrowAnyException();
    }

    @DisplayName("price 검사 - 실패 - PriceQuantity들의 price 총 합 < otherPrice")
    @Test
    void validatePrice_Fail_When_OtherPriceIsGreaterThanTotalPrice() {
        // given
        final int priceValue = 6_000;
        final ProductQuantities productQuantities = 상품이_한_개만_존재하는_ProductQuantities를_생성한다(priceValue);

        // when
        // then
        assertThatThrownBy(() ->
            productQuantities.validateTotalPriceIsGreaterOrEqualThan(new Price(priceValue + 1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private ProductQuantities 상품이_한_개만_존재하는_ProductQuantities를_생성한다(int priceValue) {
        final Product product = new Product("햄버거", priceValue);
        final Quantity quantity = new Quantity(1L);

        final ProductQuantities productQuantities = new ProductQuantities();
        productQuantities.add(new ProductQuantity(product, quantity));
        return productQuantities;
    }
}
