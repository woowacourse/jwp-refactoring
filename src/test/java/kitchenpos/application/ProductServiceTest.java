package kitchenpos.application;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("Product 를 생성한다.")
    void createProduct_success() {
        // given
        Product product = new Product("상품", BigDecimal.ONE);

        // when
        Long id = productService.create(product)
                .getId();

        // then
        Product savedProduct = productDao.findById(id)
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(product.getPrice().compareTo(savedProduct.getPrice())).isZero()
        );
    }

    @Test
    @DisplayName("price 가 null 인 경우 저장에 실패한다.")
    void createProduct_failByPriceIsNull() {
        // given
        Product product = new Product("price 가 null 인 상품", null);

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("price 가 음수인 경우 저장에 실패한다.")
    void createProduct_failByPriceLowerThanZero() {
        // given
        Product product = new Product("price 가 0원 미만인 상품", BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
