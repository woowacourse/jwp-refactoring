package kitchenpos.application;

import static kitchenpos.fixture.domain.ProductFixture.짜장면;
import static kitchenpos.fixture.domain.ProductFixture.짬뽕;
import static kitchenpos.fixture.domain.ProductFixture.탕수육;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        final ProductRequest request = new ProductRequest("짜장면", 10_000L);

        // when
        final ProductResponse actual = productService.create(request);

        // then
        final Long actualId = actual.getId();
        assertAll(
                () -> assertThat(actualId).isNotNull(),
                () -> assertThat(productDao.findById(actualId)).isPresent()
        );
    }

    @Test
    @DisplayName("create : 상품의 가격이 0원 미만이면 예외가 발생한다.")
    void create_invalidPrice_throwException() {
        // given
        final ProductRequest request = new ProductRequest("짜장면", -1L);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void list() {
        // given
        상품등록(짜장면);
        상품등록(짬뽕);
        상품등록(탕수육);

        // when
        final List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual).hasSize(3);
    }
}
