package kitchenpos.application;

import kitchenpos.application.test.IntegrateServiceTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static kitchenpos.domain.fixture.ProductFixture.치킨_생성;
import static kitchenpos.domain.fixture.ProductFixture.피자_생성;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest extends IntegrateServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Nested
    class 상품을_생성한다 {
        @Test
        void 상품을_정상적으로_생성한다() {
            // given
            Product 상품 = 치킨_생성();

            // when, then
            assertDoesNotThrow(() -> productService.create(상품));
        }

        @Test
        void 가격이_Null이면_예외가_발생한다() {
            // given
            Product 상품 = 치킨_생성();
            상품.setPrice(null);

            // when, then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.create(상품)
            );
        }

        @Test
        void 가격이_0보다_낮으면_예외가_발생한다() {
            // given
            Product 상품 = 치킨_생성();
            상품.setPrice(valueOf(-1000));

            // when, then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.create(상품)
            );
        }
    }

    @Nested
    class 상품_목록을_조회한다 {

        @BeforeEach
        void setUp() {
            Product 치킨 = 치킨_생성();
            Product 피자 = 피자_생성();

            productDao.save(치킨);
            productDao.save(피자);
        }

        @Test
        void 상품_목록을_조회한다() {
            // when
            List<Product> products = productService.list();

            // then
            Assertions.assertAll(
                    () -> assertThat(products).hasSize(2),
                    () -> assertThat(products).extracting(Product::getName)
                            .contains("치킨", "피자"),
                    () -> assertThat(products).extracting(Product::getPrice)
                            .allSatisfy(price -> assertThat(price).isGreaterThanOrEqualTo(BigDecimal.valueOf(0)))
            );
        }

    }

}