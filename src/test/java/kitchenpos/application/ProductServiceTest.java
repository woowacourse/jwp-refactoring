package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("새로운 상품을 등록할 수 있다.")
    @Test
    void create() {
        final Product product = new Product();
        product.setName("치킨마요");
        product.setPrice(new BigDecimal(3500));
        assertThatCode(() -> productService.create(product))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품을 등록할 때 상품 가격을 입력하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoPrice() {
        final Product product = new Product();
        product.setName("치킨마요");
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품을 등록할 때 상품 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifPriceUnder0() {
        final Product product = new Product();
        product.setName("치킨마요");
        product.setPrice(new BigDecimal(-1));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        final List<Product> products = productService.list();
        assertThat(products).hasSize(6);
    }
}
