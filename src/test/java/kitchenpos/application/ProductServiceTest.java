package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("MenuGroup을 생성하고 DB에 저장한다.")
    @Test
    void createTest() {
        Product product = new Product();
        product.setName("감자");
        product.setPrice(BigDecimal.valueOf(1000L));

        Product result = productService.create(product);

        Product savedProduct = productRepository.findById(result.getId())
                .orElseThrow(() -> new NoSuchElementException("저장되지 않았습니다."));
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isZero();
    }

    @DisplayName("가격이 0원 미만이면 예외가 발생한다.")
    @Test
    void invalidPriceExceptionTest() {
        Product product = new Product();
        product.setName("토마토");
        product.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("MenuGroup 목록을 조회한다.")
    @Test
    void listTest() {
        List<Product> products = productService.list();
        assertThat(products).hasSize(productRepository.findAll().size());
    }
}