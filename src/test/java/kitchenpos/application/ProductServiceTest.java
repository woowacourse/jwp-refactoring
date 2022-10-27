package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static kitchenpos.fixture.ProductFixture.카레맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTestBase {

    @Test
    void 상품_생성() {
        // given
        Product product = 카레맛_떡볶이.toEntity();

        // when
        Product savedProduct = productService.create(product);

        // then
        Optional<Product> actual = productDao.findById(savedProduct.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 가격이_0원_미만인_상품_생성_불가능() {
        // given
        Product product = 카레맛_떡볶이.toEntity(BigDecimal.valueOf(-1));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록_조회() {
        // given
        Product curryTteokbokki = productDao.save(카레맛_떡볶이.toEntity());
        Product fireTteokbokki = productDao.save(불맛_떡볶이.toEntity());

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products)
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(curryTteokbokki, fireTteokbokki));
    }
}
