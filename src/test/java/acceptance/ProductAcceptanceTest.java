package acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void getMenus() {
        상품을_생성한다("후라이드", 19000);
        상품을_생성한다("돼지국밥", 19000);
        상품을_생성한다("피자", 19000);

        List<Product> products = 상품을_조회한다();

        assertThat(products).hasSize(3);
    }
}
