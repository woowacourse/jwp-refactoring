package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Menu 가격이 MenuProduct 들의 가격 * 수량 합보다 크다면, IAE를 던진다.")
    @Test
    void Should_ThrowIAE_When_MenuPriceIsGreaterThanSumOfProductOfPriceAndQuantity() {
        // given
        Product product1 = productRepository.save(new Product("상품1", BigDecimal.valueOf(10_000)));
        Product product2 = productRepository.save(new Product("상품2", BigDecimal.valueOf(50_000)));

        MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 5L);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1L);

        // when & then
        assertThatThrownBy(() -> menuValidator.validatePrice(List.of(menuProduct1, menuProduct2),
                new Price(BigDecimal.valueOf(1_000_000)))).isInstanceOf(IllegalArgumentException.class);
    }
}
