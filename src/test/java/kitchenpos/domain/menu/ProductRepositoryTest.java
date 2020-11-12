package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DataJpaTest
public class ProductRepositoryTest {
    private static final String 상품_이름_후라이드치킨 = "후라이드 치킨";
    private static final String 상품_이름_코카콜라 = "코카콜라";
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.00");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.00");

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Product를 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        Product product = new Product(상품_이름_후라이드치킨, 상품_가격_15000원);

        Product savedProduct = productRepository.save(product);
        Long size = productRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedProduct.getId()).isEqualTo(1L);
        assertThat(savedProduct.getName()).isEqualTo(상품_이름_후라이드치킨);
        assertThat(savedProduct.getPrice()).isEqualTo(상품_가격_15000원);
    }

    @DisplayName("Product의 목록 조회를 요청할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        Product product1 = new Product(상품_이름_후라이드치킨, 상품_가격_15000원);
        Product product2 = new Product(상품_이름_코카콜라, 상품_가격_1000원);
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products.get(0).getId()).isEqualTo(1L);
        assertThat(products.get(0).getName()).isEqualTo(상품_이름_후라이드치킨);
        assertThat(products.get(0).getPrice()).isEqualTo(상품_가격_15000원);
        assertThat(products.get(1).getId()).isEqualTo(2L);
        assertThat(products.get(1).getName()).isEqualTo(상품_이름_코카콜라);
        assertThat(products.get(1).getPrice()).isEqualTo(상품_가격_1000원);
    }
}
