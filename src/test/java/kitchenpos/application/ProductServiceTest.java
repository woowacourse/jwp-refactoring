package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductServiceTest {

    @Nested
    class 상품_등록 {

        @ParameterizedTest
        @NullAndEmptySource
        void 이름이_공백이나_비어있다면_예외가_발생한다(String invalidName) {
            // given
            final var productDao = mock(ProductDao.class);
            final var productService = new ProductService(productDao);
            final var productWithInvalidName = createProduct(invalidName, BigDecimal.valueOf(1000));
            when(productDao.save(any(Product.class))).thenReturn(productWithInvalidName);

            // when
            final ThrowingCallable throwingCallable = () -> productService.create(productWithInvalidName);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이름이_255자_이내가_아니라면_예외가_발생한다() {
            // given
            final var productDao = mock(ProductDao.class);
            final var productService = new ProductService(productDao);
            final var invalidName = "름".repeat(256);
            final var productWithInvalidName = createProduct(invalidName, BigDecimal.valueOf(1000));
            when(productDao.save(any(Product.class))).thenReturn(productWithInvalidName);

            // when
            final ThrowingCallable throwingCallable = () -> productService.create(productWithInvalidName);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1", "0", "12345123451234512345"})
        void 가격이_null이거나_1에서_19자리의_양수가_아니라면_예외가_발생한다(BigDecimal price) {
            // given
            final var productDao = mock(ProductDao.class);
            final var productService = new ProductService(productDao);
            final var productWithInvalidPrice = createProduct("validName", price);
            when(productDao.save(any(Product.class))).thenReturn(productWithInvalidPrice);

            // when
            final ThrowingCallable throwingCallable = () -> productService.create(productWithInvalidPrice);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 정상적인_이름과_가격을_가진다면_상품이_등록된다() {
            // given
            final var productDao = mock(ProductDao.class);
            final var productService = new ProductService(productDao);
            final var validName = "validName";
            final var validPrice = BigDecimal.valueOf(1000);
            final var productWithValidNameAndPrice = createProduct(validName, validPrice);
            when(productDao.save(any(Product.class))).thenReturn(productWithValidNameAndPrice);

            // when
            final ThrowingSupplier<Product> throwingSupplier = () -> productService.create(productWithValidNameAndPrice);

            // then
            assertAll(
                    () -> Assertions.assertDoesNotThrow(throwingSupplier),
                    () -> verify(productDao, only()).save(any(Product.class))
            );
        }

        private Product createProduct(final String name, final BigDecimal price) {
            final var productWithInvalidName = new Product();
            productWithInvalidName.setName(name);
            productWithInvalidName.setPrice(price);
            return productWithInvalidName;
        }
    }

    @Nested
    class 상품_조회 {

            @Test
            void 상품_목록을_조회한다() {
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
