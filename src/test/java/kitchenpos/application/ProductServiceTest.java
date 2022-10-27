package kitchenpos.application;

import static kitchenpos.fixture.ProductBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductBuilder;
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
        Product product = aProduct()
                .withPrice(null)
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격은 음수일 수 없다")
    void throwException_WhenPriceNegative() {
        // given
        Product product = aProduct()
                .withPrice(BigDecimal.valueOf(-1L))
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Product를 생성한다")
    void delegateSaveAndReturnSavedEntity() {
        // given
        Product product = aProduct().build();

        // when
        Product savedProduct = sut.create(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("Product 목록을 조회한다")
    void returnAllSavedEntities() {
        List<Product> expected = productDao.findAll();

        List<Product> actual = sut.list();

        assertThat(actual).isEqualTo(expected);
    }
}
