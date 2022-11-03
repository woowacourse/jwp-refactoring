package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static kitchenpos.fixture.ProductFixture.카레맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.dto.ProductRequest;
import kitchenpos.support.ServiceTestBase;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTestBase {

    @Test
    void 상품_생성() {
        // given
        ProductRequest request = 카레맛_떡볶이.toRequest();

        // when
        ProductResponse response = productService.create(request);

        // then
        Optional<Product> actual = productDao.findById(response.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 가격이_0원_미만인_상품_생성_불가능() {
        // given
        ProductRequest request = 카레맛_떡볶이.toRequest(-1);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록_조회() {
        // given
        Product curryTteokbokki = productDao.save(카레맛_떡볶이.toEntity());
        Product fireTteokbokki = productDao.save(불맛_떡볶이.toEntity());
        List<Product> products = Arrays.asList(curryTteokbokki, fireTteokbokki);

        // when
        List<ProductResponse> response = productService.list();

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(toDtos(products));
    }

    private List<ProductResponse> toDtos(final List<Product> products) {
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
