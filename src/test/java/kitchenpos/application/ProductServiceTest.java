package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: 2023/10/12 helper 클래스 만들기
@SpringBootTest
@Transactional
class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Test
  @DisplayName("상품을 등록할 수 있다.")
  void create_success() {
    //given
    final Product product = new Product();
    product.setName("깐풍 치킨");
    product.setPrice(BigDecimal.valueOf(1000));

    //when
    final Product actual = productService.create(product);

    //then
    assertThat(actual).isNotNull();

  }

  @Test
  @DisplayName("상품을 등록할 때 상품의 가격이 0보다 작을 경우 예외를 반환한다.")
  void create_fail_negative_price() {
    final Product product = new Product();
    product.setPrice(BigDecimal.valueOf(-1));

    //when
    final ThrowingCallable actual = () -> productService.create(product);

    //then
    assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);

  }

  @Test
  @DisplayName("등록된 상품 목록을 조회할 수 있다.")
  void list_success() {
    // TODO: 2023/10/12 Fixture로 만들기
    // given, when
    final List<Product> actual = productService.list();

    //then
    assertThat(actual).hasSize(6);
  }
}