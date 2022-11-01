package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성할 때")
    @Nested
    class Create extends IntegrationTest {

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Product actual = productService.create("상품1", 1000L);

            // then
            assertThat(actual.getName()).isEqualTo("상품1");
        }

        @DisplayName("가격이 0원미만이면 예외를 발생시킨다.")
        @Test
        void priceLessThanZero_exception() {
            // then
            assertThatThrownBy(() -> productService.create("상품", -1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 null이면 예외를 발생시킨다.")
        @Test
        void priceIs_exception() {
            // then
            assertThatThrownBy(() -> productService.create("상품1", null))
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
