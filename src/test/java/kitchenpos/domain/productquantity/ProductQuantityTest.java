package kitchenpos.domain.productquantity;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.price.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductQuantity 도메인 단위테스트")
class ProductQuantityTest {

    @DisplayName("Product의 가격 * Quantity 반환")
    @Test
    void getTotalPrice() {
        // given
        final Product product = new Product("햄버거", 6_000);
        final Quantity quantity = new Quantity(3L);
        final ProductQuantity productQuantity = new ProductQuantity(product, quantity);
        final int expectedResult = product.getPriceAsInt() * (int) quantity.getValue();

        // when
        final Price totalPrice = productQuantity.getTotalPrice();

        // then
        assertThat(totalPrice.getValueAsInt()).isEqualTo(expectedResult);
    }
}
