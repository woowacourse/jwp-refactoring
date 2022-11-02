package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.support.application.ServiceTestEnvironment;
import kitchenpos.support.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductValidationServiceImplTest extends ServiceTestEnvironment {

    @Autowired
    private ProductValidationService productValidationService;

    @Test
    @DisplayName("전달받은 ID의 수량 가격 합을 구한다.")
    void calculateAmountSum() {
        // given
        final Product product1 = ProductFixture.createWithPrice(100L);
        final Product product2 = ProductFixture.createWithPrice(200L);

        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        // when
        final Price actual = productValidationService.calculateAmountSum(
                        Arrays.asList(savedProduct1.getId(), savedProduct2.getId()))
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(new Price(300L));
    }

    @ParameterizedTest
    @CsvSource(value = {"1,2,true", "1,3,false", "2,3,false"})
    @DisplayName("전달받은 ID가 저장되어 있는지 확인한다.")
    void existsAllByIdIn(final Long id1, final Long id2, final boolean expect) {
        // given
        final Product product1 = ProductFixture.createWithPrice(100L);
        final Product product2 = ProductFixture.createWithPrice(200L);

        serviceDependencies.save(product1);
        serviceDependencies.save(product2);

        // when
        final boolean actual = productValidationService.existsProductsByIdIn(Arrays.asList(id1, id2));

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
