package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductDao productDao;

    @Autowired
    ProductService sut;

    @Test
    @DisplayName("가격은 null일 수 없다")
    void throwException_WhenPriceNull() {
        // given
        Product product = new Product();
        product.setName("강정치킨");

        // when && then
        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 음수일 수 없다")
    void throwException_WhenPriceNegative() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(-1L));

        // when && then
        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Product를 생성한다")
    void delegateSaveAndReturnSavedEntity() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(1000L));

        // when
        Product savedProduct = sut.create(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    @DisplayName("Product 목록을 조회한다")
    void returnAllSavedEntities() {
        List<Product> actual = sut.list();

        assertThat(actual).hasSize(6);
    }
}
