package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.맛초킹_20000_REQUEST;
import static kitchenpos.fixture.ProductFixture.뿌링클_19000_REQUEST;
import static kitchenpos.fixture.ProductFixture.뿌링클_NULL_REQUEST;
import static kitchenpos.fixture.ProductFixture.뿌링클_UNDER_ZERO_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void beforeEach() {
        this.productService = new ProductService(new FakeProductDao());
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // when
        Product newProduct = productService.create(뿌링클_19000_REQUEST);

        // then
        assertThat(newProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품을 생성 시 가격이 null이면 예외를 반환한다.")
    void create_WhenNullPrice() {
        // when & then
        assertThatThrownBy(() -> productService.create(뿌링클_NULL_REQUEST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 공백이거나 0원보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("상품 생성 시 가격이 0보다 작으면 예외를 반환한다.")
    void create_WhenPriceUnderZero() {
        // when & then
        assertThatThrownBy(() -> productService.create(뿌링클_UNDER_ZERO_REQUEST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 공백이거나 0원보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void list() {
        // given
        productService.create(뿌링클_19000_REQUEST);
        productService.create(맛초킹_20000_REQUEST);

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products.size()).isEqualTo(2);
    }
}
