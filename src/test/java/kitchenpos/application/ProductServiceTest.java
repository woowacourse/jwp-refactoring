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

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("상품을 추가할 때, 상품의 가격이")
    class Create {

        @Test
        @DisplayName("0원 이상인 경우 정상 등록")
        void success() {
            // given
            Product product = ProductFixture.create("제이슨의 무료 나눔 마우스", 0);

            // when
            Product savedProduct = productService.create(product);
            Long productId = savedProduct.getId();

            // then
            assertThat(productDao.findById(productId)).isPresent();
        }

        @Test
        @DisplayName("0원 미만인 경우, 예외 발생")
        void fail() {
            // given
            Product product = ProductFixture.create("준팍의 고장난 맥북", -30_000);

            // expect
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    @DisplayName("상품 목록 조회")
    void list() {
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
