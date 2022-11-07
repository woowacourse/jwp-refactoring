package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.menu.dto.request.CreateProductDto;
import kitchenpos.application.menu.dto.response.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 생성한_상품을_반환한다() {
            CreateProductDto product = new CreateProductDto("강정치킨", BigDecimal.valueOf(1000));

            ProductDto actual = productService.create(product);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo("강정치킨"),
                    () -> assertThat(actual.getPrice()).isNotNull()
            );
        }

        @Test
        void 가격정보가_누락된_경우_예외발생() {
            CreateProductDto product = new CreateProductDto("강정치킨", null);

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품명이_누락된_경우_예외발생() {
            CreateProductDto product = new CreateProductDto(null, BigDecimal.valueOf(1000));

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void list_메서드는_상품_목록을_조회한다() {
        List<ProductDto> products = productService.list();

        assertThat(products).hasSizeGreaterThan(1);
    }
}
