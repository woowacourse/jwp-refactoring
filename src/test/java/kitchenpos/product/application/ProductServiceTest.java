package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.common.domain.Money;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
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
}
