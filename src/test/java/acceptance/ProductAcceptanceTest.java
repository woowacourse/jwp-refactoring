package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void getMenus() {
        // given
        long productId1 = 상품_생성("후라이드", 19000);
        long productId2 = 상품_생성("돼지국밥", 9000);
        long productId3 = 상품_생성("피자", 31000);
        long productId4 = 상품_생성("수육", 25000);

        // when
        List<Product> products = 상품_조회();

        // then
        assertThat(products)
                .extracting(Product::getId, Product::getName, p -> p.getPrice().intValueExact())
                .containsExactlyInAnyOrder(
                        tuple(productId1, "후라이드", 19000),
                        tuple(productId2, "돼지국밥", 9000),
                        tuple(productId3, "피자", 31000),
                        tuple(productId4, "수육", 25000)
                );
    }
}
