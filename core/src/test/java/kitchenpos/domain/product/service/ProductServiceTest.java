package kitchenpos.domain.product.service;

import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.service.ProductService;
import kitchenpos.product.service.dto.ProductCreateRequest;
import kitchenpos.product.service.dto.ProductResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static kitchenpos.domain.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    class Create {

        @Test
        void 상품을_생성할_수_있다() {
            // given
            final ProductCreateRequest normalRequest = new ProductCreateRequest("치킨", 20_000L);
            final Product expected = spy(product(normalRequest.getName(), BigDecimal.valueOf(normalRequest.getPrice())));
            given(productRepository.save(any(Product.class))).willReturn(expected);

            final long savedId = 1L;
            given(expected.getId()).willReturn(savedId);

            // when
            final ProductResponse actual = productService.create(normalRequest);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                    () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice().getPrice().longValue())
            );
        }

        @Test
        void 상품의_가격이_0보다_작으면_상품을_생성할_수_없다() {
            // given
            final ProductCreateRequest underZeroPriceRequest = new ProductCreateRequest("치킨", -1L);

            // when, then
            assertThatThrownBy(() -> productService.create(underZeroPriceRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 상품을_전체_조회할_수_있다() {
            // when
            productService.list();

            // then
            verify(productRepository, only()).findAll();
        }
    }
}
