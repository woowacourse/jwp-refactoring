package kitchenpos.domain.menu;

import kitchenpos.dto.ProductQuantityRequest;
import kitchenpos.dto.ProductQuantityRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductQuantityRequestsTest {
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final Long 상품_1개 = 1L;
    private static final Long 상품_2개 = 2L;
    private List<ProductQuantityRequest> productQuantities;

    @BeforeEach
    void setUp() {
        productQuantities = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개),
                new ProductQuantityRequest(상품_ID_2, 상품_2개)
        );
    }

    @DisplayName("ProductQuantityRequests 일급 컬렉션이 올바르게 생성된다.")
    @Test
    void makeClassTest() {
        ProductQuantityRequests productQuantityRequests = new ProductQuantityRequests(productQuantities);

        assertThat(productQuantityRequests.getProductIds()).
                hasSize(2).
                containsOnly(상품_ID_1, 상품_ID_2);
        assertThat(productQuantityRequests.getProductQuantityMatcher()).hasSize(2);
        assertThat(productQuantityRequests.getProductQuantityMatcher().get(상품_ID_1)).isEqualTo(상품_1개);
        assertThat(productQuantityRequests.getProductQuantityMatcher().get(상품_ID_2)).isEqualTo(상품_2개);
    }

    @DisplayName("예외 테스트: ProductQuantityRequests에 비어있는 목록이 전달되면, 예외가 발생한다.")
    @Test
    void emptyListTest() {
        assertThatThrownBy(() -> new ProductQuantityRequests(Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 상품 개수 목록이 입력되었습니다.");
    }

    @DisplayName("예외 테스트: ProductQuantityRequests에 null 목록이 전달되면, 예외가 발생한다.")
    @Test
    void nullListTest() {
        assertThatThrownBy(() -> new ProductQuantityRequests(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 상품 개수 목록이 입력되었습니다.");
    }
}
