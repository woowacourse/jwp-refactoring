package kitchenpos.dao;

import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_등록하면_ID를_부여받는다() {
        Product 등록되지_않은_상품 = 새로운_상품("상품", new BigDecimal("10000.00"));

        Product 등록된_상품 = productRepository.save(등록되지_않은_상품);

        assertSoftly(softly -> {
            softly.assertThat(등록된_상품.getId()).isNotNull();
            softly.assertThat(등록된_상품).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(등록되지_않은_상품);
        });
    }

    @Test
    void ID로_상품을_조회한다() {
        Product 상품 = productRepository.save(새로운_상품("상품", new BigDecimal(10000)));

        Product ID로_조회한_상품 = productRepository.findById(상품.getId())
                .orElseGet(Assertions::fail);

        assertThat(ID로_조회한_상품).usingRecursiveComparison()
                .isEqualTo(상품);
    }

    @Test
    void 존재하지_않는_ID로_상품을_조회하면_Optional_empty를_반환한다() {
        Optional<Product> 존재하지_않는_ID로_조회한_상품 = productRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_상품).isEmpty();
    }

    @Test
    void 모든_상품을_조회한다() {
        Product 상품1 = productRepository.save(새로운_상품("상품1", new BigDecimal(10000)));
        Product 상품2 = productRepository.save(새로운_상품("상품2", new BigDecimal(20000)));

        List<Product> 모든_상품 = productRepository.findAll();

        assertThat(모든_상품).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(상품1, 상품2);
    }
}
