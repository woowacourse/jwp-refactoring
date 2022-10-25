package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("id에 포함된 모든 Product를 조회한다.")
    void findAllByIdIn() {
        Product product1 = productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));
        productRepository.save(new Product("other", BigDecimal.valueOf(1)));

        List<Product> products = productRepository.findAllByIdIn(List.of(product1.getId(), product2.getId()));

        assertThat(products).containsExactly(product1, product2);
    }

}
