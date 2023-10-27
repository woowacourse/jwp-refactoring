package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.Money;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductUpdateRequest;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Nested
    class create {

        @Test
        void 성공() {
            // given
            var request = new ProductCreateRequest("소주", 5000);

            // when
            var response = productService.create(request);

            // then
            assertThat(response.getName()).isEqualTo("소주");
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            List<Product> expect = List.of(
                new Product(1L, "소주", Money.ZERO),
                new Product(2L, "맥주주", Money.ZERO)
            );
            given(productRepository.findAll())
                .willReturn(expect);

            // when
            var response = productService.findAll();

            // then
            assertThat(response).hasSize(2);
        }
    }

    @Nested
    class update {

        @Test
        void 요청의_가격이_null이면_가격을_수정하지_않는다() {
            // given
            given(productRepository.findById(1L))
                .willReturn(Optional.of(new Product(1L, "소주", Money.from(2000L))));

            // when
            var response = productService.update(1L, new ProductUpdateRequest("맥주", null));

            // then
            assertThat(response.getName()).isEqualTo("맥주");
            assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(2000));
        }

        @Test
        void 요청의_이름이_null이면_이름을_수정하지_않는다() {
            // given
            given(productRepository.findById(1L))
                .willReturn(Optional.of(new Product(1L, "소주", Money.from(2000L))));

            // when
            var response = productService.update(1L, new ProductUpdateRequest(null, 4885L));

            // then
            assertThat(response.getName()).isEqualTo("소주");
            assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(4885));
        }

        @Test
        void 성공() {
            // given
            given(productRepository.findById(1L))
                .willReturn(Optional.of(new Product(1L, "소주", Money.from(2000L))));

            // when
            var response = productService.update(1L, new ProductUpdateRequest("맥주", 4885L));

            // then
            assertThat(response.getName()).isEqualTo("맥주");
            assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(4885));
        }
    }
}
