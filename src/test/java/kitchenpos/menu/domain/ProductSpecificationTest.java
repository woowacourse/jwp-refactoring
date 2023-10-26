package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.vo.Price;
import kitchenpos.menu.vo.ProductSpecification;
import kitchenpos.menu.vo.Quantity;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductSpecificationTest {

    @Test
    void 총액을_계산한다() {
        // given
        ProductSpecification productSpecification = ProductSpecification.from("productName", BigDecimal.ONE);
        Quantity quantity = Quantity.valueOf(1L);

        // when
        Price price = productSpecification.calculateTotalPrice(quantity);

        // then
        assertThat(price).isEqualTo(Price.valueOf(BigDecimal.ONE));
    }
}
