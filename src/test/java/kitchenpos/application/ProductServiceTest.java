package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 테스트")
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("Product의 Price가 null이라면")
        class Context_with_price_null {

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                Product product = new Product();
                product.setName("후라이드 치킨");
                product.setPrice(null);

                // when, then
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class);
                then(productDao)
                        .should(never())
                        .save(product);
            }
        }

        @Nested
        @DisplayName("Product의 Price가 음수라면")
        class Context_with_price_negative {
            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                // given
                Product product = new Product();
                product.setName("후라이드 치킨");
                product.setPrice(BigDecimal.valueOf(-1000L));

                // when, then
                assertThatThrownBy(() -> productService.create(product))
                        .isInstanceOf(IllegalArgumentException.class);
                then(productDao)
                        .should(never())
                        .save(product);
            }
        }

        @Nested
        @DisplayName("정상적인 경우라면")
        class Context_with_correct_case {

            @Test
            @DisplayName("Product를 반환한다")
            void it_return_product() {
                // given
                Product product = new Product();
                product.setName("후라이드 치킨");
                product.setPrice(BigDecimal.valueOf(18000));
                given(productDao.save(product)).willReturn(product);

                // when
                final Product createdProduct = productService.create(product);

                // then
                assertThat(createdProduct)
                        .isEqualTo(product);
                then(productDao)
                        .should(times(1))
                        .save(product);
            }
        }

    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Test
        @DisplayName("Product 리스트를 반환한다.")
        void it_return_product_list() {
            // given
            Product product1 = new Product();
            product1.setName("후라이드 치킨");
            product1.setPrice(BigDecimal.valueOf(18000));

            Product product2 = new Product();
            product2.setName("양념 치킨");
            product2.setPrice(BigDecimal.valueOf(19000));

            given(productDao.findAll())
                    .willReturn(Arrays.asList(product1, product2));

            // when
            List<Product> products = productService.list();

            // then
            assertThat(products).hasSize(2);
            assertThat(products).containsExactly(product1, product2);
            then(productDao)
                    .should(times(1))
                    .findAll();
        }
    }

}