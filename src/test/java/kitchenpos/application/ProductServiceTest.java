package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Nested
    class 상품을_추가할_때_상품의 {

        @Test
        void 가격이_0원_이상인_경우_정상_등록() {
            // given
            Product product = ProductFixture.create("제이슨의 무료 나눔 마우스", 0);

            // when
            Product savedProduct = productService.create(product);
            Long productId = savedProduct.getId();

            // then
            assertThat(productDao.findById(productId)).isPresent();
        }

        @Test
        void 가격이_0원_미만인_경우_예외_발생() {
            // given
            Product product = ProductFixture.create("준팍의 고장난 맥북", -30_000);

            // expect
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 상품_목록_조회() {
        // given
        Product pasta = productDao.save(ProductFixture.create("파스타", 28_000));
        Product steak = productDao.save(ProductFixture.create("스테이크", 60_000));

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(pasta, steak));
    }
}
