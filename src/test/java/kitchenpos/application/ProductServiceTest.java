package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = "/data-initialization-h2.sql")
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @DisplayName("[상품 생성] 상품을 정상적으로 생성한다.")
    @Test
    void create() {
        // given
        Product product = TestFixtureFactory.상품_후라이드_치킨();

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger());
    }

    @DisplayName("[상품 생성] 상품 생성 시 가격이 null 이면 예외가 발생한다.")
    @Test
    void createWithNullPrice() {
        // given
        Product product = TestFixtureFactory.상품_생성("가격이 null인 상품", null);

        // when then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[상품 생성] 상품 생성 시 가격이 음수라면 예외가 발생한다.")
    @Test
    void createWithNegativePrice() {
        // given
        Product product = TestFixtureFactory.상품_생성("가격이 음수인 상품", new BigDecimal(-1));

        // when then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[상품 전체 조회] 상품 전체를 조회한다.")
    @Test
    void list() {
        // given
        Product 후라이드_치킨 = TestFixtureFactory.상품_생성("후라이드 치킨", new BigDecimal(16000));
        Product savedProduct1 = productService.create(후라이드_치킨);
        Product 양념_치킨 = TestFixtureFactory.상품_생성("양념 치킨", new BigDecimal(17000));
        Product savedProduct2 = productService.create(양념_치킨);

        // when
        List<Product> findProducts = productService.list();

        // then
        isSameProduct(findProducts.get(0), savedProduct1);
        isSameProduct(findProducts.get(1), savedProduct2);
    }

    private void isSameProduct(Product product, Product otherProduct) {
        assertThat(product.getId()).isEqualTo(otherProduct.getId());
        assertThat(product.getName()).isEqualTo(otherProduct.getName());
        assertThat(product.getPrice()).isEqualTo(otherProduct.getPrice());
    }
}