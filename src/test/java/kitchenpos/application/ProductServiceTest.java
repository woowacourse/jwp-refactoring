package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.support.ServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

class ProductServiceTest extends ServiceIntegrationTest {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductDao productDao;

  @Test
  @DisplayName("create() : 물품을 생성할 수 있다.")
  void test_create() throws Exception {
    //given
    final Product product = new Product();
    product.setName("product");
    product.setPrice(BigDecimal.valueOf(10000));

    //when
    final Product savedProduct = productService.create(product);

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
    final Product product = new Product();
    product.setName("product");
    product.setPrice(BigDecimal.valueOf(10000));

    final int beforeSize = productService.list().size();

    productDao.save(product);

    //when
    final List<Product> products = productService.list();

    //then
    assertEquals(products.size(), beforeSize + 1);
  }
}
