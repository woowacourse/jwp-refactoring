package kitchenpos.service;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.ProductRepository;
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

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Product Repository 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Product 추가 테스트 - 성공")
    @Test
    void create() {
        //given
        Product product = ProductFixture.create();
        //when
        Product create = productRepository.save(product);
        //then
        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("모든 Product 반환")
    @Test
    void list() {
        //given
        Product product = ProductFixture.create();
        productRepository.save(product);
        //when
        //when
        List<Product> products = productRepository.findAll();
        //then
        assertThat(products).isNotEmpty();
    }
}
