package kitchenpos.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.test.ServiceTest;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService sut;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 상품을_추가할_때 {

        @Test
        void 상품의_가격이_0원_미만인_경우_예외를_던진다() {
            // given
            ProductRequest productRequest = new ProductRequest(null, "피자", BigDecimal.valueOf(-1L));

            // expect
            assertThatThrownBy(() -> sut.create(productRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 가격은 0원 이상이어야 합니다.");
        }

        @Test
        void 상품의_가격이_0원_이상인_경우_정상적으로_등록된다() {
            // given
            ProductRequest productRequest = new ProductRequest(null, "무료 피자", BigDecimal.valueOf(0L));

            // when
            ProductResponse productResponse = sut.create(productRequest);

            // then
            assertThat(productRepository.findById(productResponse.getId())).isPresent();
        }
    }

    @Test
    void 상품_목록을_조회한다() {
        // given
        Product pizza = productRepository.save(ProductFixture.상품("피자", 8900L));
        Product chicken = productRepository.save(ProductFixture.상품("치킨", 18000L));

        // when
        List<ProductResponse> result = sut.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(List.of(ProductResponse.from(pizza), ProductResponse.from(chicken)));
    }
}
