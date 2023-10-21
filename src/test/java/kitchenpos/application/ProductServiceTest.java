package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.CreateProductCommand;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;


    @Nested
    class 상품_생성 {
        @Test
        void 상품을_생성할_수_있다() {
            //given
            CreateProductCommand 상품_생성_요청 = new CreateProductCommand("상품명", BigDecimal.valueOf(1000));

            //when
            Product 생성된_상품 = productService.create(상품_생성_요청);

            //then
            assertAll(
                    () -> assertThat(생성된_상품.getId()).isNotNull(),
                    () -> assertThat(생성된_상품.getName()).isEqualTo(생성된_상품.getName()),
                    () -> assertThat(생성된_상품.getPrice().compareTo(생성된_상품.getPrice())).isEqualTo(0)
            );
        }

        @Test
        void 상품가격이_null이면_예외가_발생한다() {
            //given
            CreateProductCommand 상품_생성_요청 = new CreateProductCommand("상품명", null);

            //expect
            assertThatThrownBy(() -> productService.create(상품_생성_요청))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {-1, -1000})
        void 상품가격이_음수이면_예외가_발생한다(Long price) {
            //given
            CreateProductCommand 상품_생성_요청 = new CreateProductCommand("상품명", BigDecimal.valueOf(price));

            //expect
            assertThatThrownBy(() -> productService.create(상품_생성_요청))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 상품_리스트를_조회할_수_있다() {
        //given
        List<Long> allProductIds = productRepository.findAll().stream()
                .map(Product::getId)
                .collect(toList());

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).extracting(Product::getId)
                .containsAll(allProductIds);
    }

}
