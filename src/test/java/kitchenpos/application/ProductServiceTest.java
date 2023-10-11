package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_저장_성공() {
        // given
        Product expected = new Product("고추바사삭", "20000.00");

        // when
        Product actual = productService.create(expected);

        // then
        assertAll(
            () -> assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected),
            () -> assertThat(actual.getId()).isPositive()
        );
    }

    @Test
    void 상품_저장_실패_가격이_음수() {
        // given
        Product expected = new Product("고추바사삭", "-1");

        // when && then
        assertThatThrownBy(() -> productService.create(expected))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품_조회() {
        // given
        List<Product> expected = new ArrayList<>();
        expected.add(productDao.save(new Product("고추바사삭", "10000.00")));
        expected.add(productDao.save(new Product("뿌링클", "20000.00")));
        expected.add(productDao.save(new Product("맛초킹", "30.00")));

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
