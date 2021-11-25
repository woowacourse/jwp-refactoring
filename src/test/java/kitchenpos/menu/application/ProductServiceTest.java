package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.exception.InvalidPriceException;
import kitchenpos.menu.ui.dto.request.ProductRequestDto;
import kitchenpos.menu.ui.dto.response.ProductResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("메뉴 그룹 서비스 통합 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성 테스트")
    @Nested
    class ProductCreate {

        @DisplayName("[성공] 새로운 상품 등록")
        @Test
        void create_Success() {
            // given
            ProductRequestDto product = newProduct();

            // when
            ProductResponseDto savedProduct = productService.create(product);

            // then
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct)
                .extracting(in -> tuple(in.getName(), in.getPrice().toBigInteger()))
                .isEqualTo(tuple(product.getName(), product.getPrice().toBigInteger()));
        }

        @DisplayName("[실패] 가격이 null 이면 예외 발생")
        @Test
        void create_NullPrice_ExceptionThrown() {
            // given
            ProductRequestDto product = newProduct(null);

            // when
            // then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(InvalidPriceException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 가격이 음수면 예외 발생")
        @Test
        void create_NegativePrice_ExceptionThrown() {
            // given
            ProductRequestDto product = newProduct(BigDecimal.valueOf(-10));

            // when
            // then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(InvalidPriceException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
            ;
        }
    }

    @DisplayName("[성공] 전체 상품 조회")
    @Test
    void list_Success() {
        // given
        int previousSize = productService.list().size();
        productService.create(newProduct());

        // when
        List<ProductResponseDto> result = productService.list();

        // then
        assertThat(result).hasSize(previousSize + 1);
    }

    private ProductRequestDto newProduct() {
        return newProduct(BigDecimal.valueOf(25_000));
    }

    private ProductRequestDto newProduct(BigDecimal price) {
        return new ProductRequestDto("새로운 상품", price);
    }
}
