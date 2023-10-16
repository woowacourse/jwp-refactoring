package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import kitchenpos.domain.Product2;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@JdbcTest(includeFilters = @ComponentScan.Filter(Repository.class))
class ProductRepositoryImplTest {

  @Autowired
  private ProductRepositoryImpl productRepository;

  @Test
  @DisplayName("create() : 물품을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Product2 product = ProductFixture.createProduct();

    //when
    final Product2 savedProduct = productRepository.save(product);

    //then
    assertAll(
        () -> assertNotNull(savedProduct.getId()),
        () -> assertEquals(product.getName(), savedProduct.getName()),
        () -> assertEquals(0, product.getPrice().compareTo(savedProduct.getPrice()))
    );
  }
}
