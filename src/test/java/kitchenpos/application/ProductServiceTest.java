package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import kitchenpos.dao.ProductRepositoryImpl;
import kitchenpos.domain.Product2;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceIntegrationTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepositoryImpl productRepository;

  @Test
  @DisplayName("create() : 물품을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Product2 product = ProductFixture.createProduct();

    //when
    final Product2 savedProduct = productService.create(product);

    //then
    assertAll(
        () -> assertNotNull(savedProduct.getId()),
        () -> assertEquals(0, savedProduct.getPrice().compareTo(product.getPrice())),
        () -> assertEquals(savedProduct.getName(), product.getName())
    );
  }

  @Test
  @DisplayName("list() : 모든 물품을 조회할 수 있다.")
  void test_list() throws Exception {
    //given
    productRepository.save(ProductFixture.createProduct());
    productRepository.save(ProductFixture.createProduct());
    productRepository.save(ProductFixture.createProduct());
    productRepository.save(ProductFixture.createProduct());
    productRepository.save(ProductFixture.createProduct());

    //when
    final List<Product2> products = productService.list();

    //then
    assertEquals(5, products.size());
  }
}
