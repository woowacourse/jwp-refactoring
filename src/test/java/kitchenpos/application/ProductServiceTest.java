package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fakedao.InMemoryProductDao;
import kitchenpos.domain.Product.Product;
import kitchenpos.domain.ProductFactory;
import kitchenpos.ui.request.ProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    private ProductDao fakeProductDao;

    @BeforeEach
    void setUp() {
        fakeProductDao = new InMemoryProductDao();
    }

    @Nested
    class 상품_등록시 {

        @Test
        void 정상적인_이름과_가격을_가진다면_상품이_등록된다() {
            // given
            final var productService = new ProductService(fakeProductDao);
            final var validName = "validName";
            final var validPrice = BigDecimal.valueOf(1000);
            final var productWithValidNameAndPrice = new ProductCreateRequest(validName, validPrice);

            // when
            final ThrowingSupplier<Product> throwingSupplier = () -> productService.create(productWithValidNameAndPrice);

            // then
            assertDoesNotThrow(throwingSupplier);
        }
    }

    @Nested
    class 상품_조회시 {

        @Test
        void 정상적으로_조회한다() {
            // given
            fakeProductDao.save(ProductFactory.createProductOf("validName", BigDecimal.valueOf(1000)));
            fakeProductDao.save(ProductFactory.createProductOf("validName2", BigDecimal.valueOf(1000)));
            final var productService = new ProductService(fakeProductDao);

            // when
            final var products = productService.list();

            // then
            assertAll(
                    () -> assertThat(products).hasSize(2),
                    () -> assertThat(products).extracting(Product::getName)
                                              .containsExactlyInAnyOrder("validName", "validName2"),
                    () -> assertThat(products).extracting(Product::getPrice)
                                              .containsExactlyInAnyOrder(BigDecimal.valueOf(1000), BigDecimal.valueOf(1000))
            );
        }
    }
}
