package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Nested
    class 상품을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            Product 상품_엔티티_A = ProductFixture.상품_엔티티_A;
            given(productDao.save(any(Product.class)))
                    .willReturn(상품_엔티티_A);

            Product response = productService.create(상품_엔티티_A);

            assertThat(response).usingRecursiveComparison().isEqualTo(상품_엔티티_A);
        }

        @Test
        void 상품_가격이_NULL_이면_예외가_발생한다() {
            Product 상품_엔티티_C_가격_Null = ProductFixture.상품_엔티티_B_가격_NULL;

            assertThatThrownBy(() -> productService.create(상품_엔티티_C_가격_Null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_가격이_음수이면_예외가_발생한다() {
            Product 상품_엔티티_가격_음수 = ProductFixture.상품_엔티티_C_가격_음수;

            assertThatThrownBy(() -> productService.create(상품_엔티티_가격_음수))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_상품을_조회한다() {
        Product 상품_엔티티_A = ProductFixture.상품_엔티티_A;
        given(productDao.findAll())
                .willReturn(List.of(상품_엔티티_A));

        List<Product> products = productService.list();

        assertThat(products).contains(상품_엔티티_A);
    }
}
