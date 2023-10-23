package kitchenpos.application;

import java.util.List;

import kitchenpos.domain.Product;
import kitchenpos.ui.dto.product.CreateProductRequest;
import kitchenpos.domain.vo.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_저장할_수_있다() {
        // given
        final CreateProductRequest createProductRequest = new CreateProductRequest("치즈피자", 7_000);

        // when
        final Product actual = productService.create(createProductRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("치즈피자"),
                () -> assertThat(actual.getPrice()).isEqualTo(Money.valueOf(7_000))
        );
    }


    @Test
    void 저장된_상품을_모두_가져올_수_있다() {
        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSize(6);
    }

}
