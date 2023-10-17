package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.product.CreateProductCommand;
import kitchenpos.application.dto.product.CreateProductResponse;
import kitchenpos.application.dto.product.SearchProductResponse;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends IntegrationTest {

    @Test
    void 상품을_저장한다() {
        // given
        CreateProductCommand command = new CreateProductCommand("상품", BigDecimal.ONE);

        // when
        CreateProductResponse response = productService.create(command);
        Optional<Product> result = productRepository.findById(response.id());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().id()).isPositive(),
                () -> assertThat(result.get().price()).isEqualByComparingTo(BigDecimal.ONE)
        );
    }

    @Test
    void 상품의_가격이_null이면_예외가_발생한다() {
        // given
        CreateProductCommand command = new CreateProductCommand("상품", null);

        // when & then
        assertThatThrownBy(() -> productService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격이_0보다_작으면_에외가_발생한다() {
        // given
        CreateProductCommand command = new CreateProductCommand("상품", BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(command))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품들을_조회한다() {
        // given
        Product product1 = new Product("상품1", new Price(BigDecimal.ONE));
        productRepository.save(product1);
        Product product2 = new Product("상품2", new Price(BigDecimal.TEN));
        productRepository.save(product2);

        // when
        List<SearchProductResponse> result = productService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).id()).isPositive(),
                () -> assertThat(result.get(0).name()).isEqualTo("상품1"),
                () -> assertThat(result.get(0).price()).isEqualByComparingTo(BigDecimal.ONE),
                () -> assertThat(result.get(1).id()).isPositive(),
                () -> assertThat(result.get(1).name()).isEqualTo("상품2"),
                () -> assertThat(result.get(1).price()).isEqualByComparingTo(BigDecimal.TEN)
        );
    }
}
