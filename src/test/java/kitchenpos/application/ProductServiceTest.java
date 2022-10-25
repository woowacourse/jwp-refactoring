package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fakedao.ProductFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductServiceTest {

    private ProductDao productDao = new ProductFakeDao();

    private ProductService productService = new ProductService(productDao);

    @DisplayName("상품을 생성할 때")
    @Nested
    class Create {

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Product product = new Product("상품1", new BigDecimal(1000));
            Product actual = productService.create(product);

            // then
            assertThat(actual.getName()).isEqualTo("상품1");
        }

        @DisplayName("가격이 0원미만이면 예외를 발생시킨다.")
        @Test
        void priceLessThanZero_exception() {
            // then
            Product product = new Product("상품1", BigDecimal.valueOf(-1));
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 null이면 예외를 발생시킨다.")
        @Test
        void priceIs_exception() {
            // then
            Product product = new Product("상품1", null);
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void list() {
        // given
        productDao.save(new Product("상품1", BigDecimal.valueOf(1000)));
        productDao.save(new Product("상품2", BigDecimal.valueOf(1000)));
        productDao.save(new Product("상품3", BigDecimal.valueOf(1000)));

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSize(3);
    }
}
