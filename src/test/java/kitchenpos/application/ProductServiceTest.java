package kitchenpos.application;

import kitchenpos.application.fixture.ProductServiceFixture;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest extends ProductServiceFixture {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Nested
    class 상품_등록 {

        @Test
        void 상품을_등록한다() {
            given(productDao.save(any())).willReturn(등록한_상품);

            final Product actual = productService.create(등록_요청_상품_dto);

            assertThat(actual).isEqualTo(등록한_상품);
        }

        @Test
        void 상품_가격이_입력되지_않은_경우_예외가_발생한다() {
            assertThatThrownBy(() -> productService.create(가격이_입력되지_않은_상품))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_가격이_0_보다_작은_경우_예외가_발생한다() {
            assertThatThrownBy(() -> productService.create(가격이_0보다_작은_상품))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 상품_조회 {

        @Test
        void 모든_상품을_조회한다() {
            given(productDao.findAll()).willReturn(저장된_모든_상품);

            final List<Product> actual = productService.list();

            assertThat(actual).hasSize(저장된_모든_상품.size());
        }
    }
}
