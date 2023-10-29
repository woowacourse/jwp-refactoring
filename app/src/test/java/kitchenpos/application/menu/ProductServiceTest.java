package kitchenpos.application.menu;

import static kitchenpos.menu.exception.PriceExceptionType.PRICE_CAN_NOT_NEGATIVE;
import static kitchenpos.menu.exception.PriceExceptionType.PRICE_CAN_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.IntegrationTest;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.menu.application.dto.CreateProductCommand;
import kitchenpos.menu.application.dto.CreateProductResponse;
import kitchenpos.menu.application.dto.SearchProductResponse;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
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
                () -> assertThat(result.get().price()).isEqualTo(new Price(BigDecimal.ONE))
        );
    }

    @Test
    void 상품의_가격이_null이면_예외가_발생한다() {
        // given
        CreateProductCommand command = new CreateProductCommand("상품", null);

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                productService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NULL);
    }

    @Test
    void 상품의_가격이_0보다_작으면_에외가_발생한다() {
        // given
        CreateProductCommand command = new CreateProductCommand("상품", BigDecimal.valueOf(-1));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                productService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NEGATIVE);
    }

    @Test
    void 상품들을_조회한다() {
        // given
        상품저장(상품("상품1", 가격(1)));
        상품저장(상품("상품2", 가격(10)));

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
