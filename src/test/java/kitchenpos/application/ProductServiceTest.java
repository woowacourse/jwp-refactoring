package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Nested
    class 상품_생성 {
        @Test
        void 상품을_생성할_수_있다() {
            //given
            Product 상품 = new Product();
            상품.setName("상품명");
            상품.setPrice(BigDecimal.valueOf(1000));

            //when
            Product 생성된_상품 = productService.create(상품);

            //then
            assertAll(
                    () -> assertThat(생성된_상품.getId()).isNotNull(),
                    () -> assertThat(생성된_상품.getName()).isEqualTo(상품.getName()),
                    () -> assertThat(생성된_상품.getPrice().compareTo(상품.getPrice())).isEqualTo(0)
            );
        }

        @Test
        void 상품가격이_null이면_예외가_발생한다() {
            //given
            Product 상품 = new Product();
            상품.setName("상품명");
            상품.setPrice(null);

            //expect
            assertThatThrownBy(() -> productService.create(상품))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {-1, -1000})
        void 상품가격이_음수이면_예외가_발생한다(Long price) {
            //given
            Product 상품 = new Product();
            상품.setName("상품명");
            상품.setPrice(BigDecimal.valueOf(price));

            //expect
            assertThatThrownBy(() -> productService.create(상품))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 상품_리스트를_조회할_수_있다() {
        //given
        List<Long> allProductIds = productDao.findAll().stream()
                .map(Product::getId)
                .collect(toList());

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).extracting(Product::getId)
                .containsAll(allProductIds);
    }

}
