package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @Mock
    private ProductDao mockProductDao;

    @Nested
    class 상품_등록시 {

        @Test
        void 정상적인_이름과_가격을_가진다면_상품이_등록된다() {
            // given
            final var productService = new ProductService(mockProductDao);
            final var validName = "validName";
            final var validPrice = BigDecimal.valueOf(1000);
            final var productWithValidNameAndPrice = ProductFactory.createProductOf(validName, validPrice);
            when(mockProductDao.save(any(Product.class))).thenReturn(productWithValidNameAndPrice);

            // when
            final ThrowingSupplier<Product> throwingSupplier = () -> productService.create(productWithValidNameAndPrice);

            // then
            assertAll(
                    () -> Assertions.assertDoesNotThrow(throwingSupplier),
                    () -> verify(mockProductDao, only()).save(any(Product.class))
            );
        }
    }

    @Nested
    class 상품_조회시 {

        @Test
        void 정상적으로_조회한다() {
            // given
            final var productDao = mock(ProductDao.class);
            final var productService = new ProductService(productDao);

            // when
            productService.list();

            // then
            verify(productDao, only()).findAll();
        }
    }
}
