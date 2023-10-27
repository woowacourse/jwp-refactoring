package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.model.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.supports.ProductFixture;
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

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Nested
    class 상품_생성 {

        @Test
        void 이름과_가격을_받아_상품을_생성한다() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("피자", new BigDecimal(1000));
            given(productRepository.save(any(Product.class)))
                .willReturn(ProductFixture.fixture().id(1L).build());

            // when
            ProductResponse actual = productService.create(request);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class 상품_전체_조회 {

        @Test
        void 전체_상품을_조회한다() {
            // given
            given(productRepository.findAll())
                .willReturn(List.of(
                    ProductFixture.fixture().id(1L).build(),
                    ProductFixture.fixture().id(2L).build()
                ));

            // when
            List<ProductResponse> actual = productService.list();

            // then
            assertThat(actual.stream().map(ProductResponse::getId)).containsExactly(1L, 2L);
        }
    }
}
