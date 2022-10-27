package kitchenpos.acceptance;

import static kitchenpos.acceptance.fixture.ProductStepDefinition.상품_목록을_조회한다;
import static kitchenpos.acceptance.fixture.ProductStepDefinition.상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    void 상품_목록을_조회할_수_있다() {
        상품을_생성한다("후라이드", 16000);
        상품을_생성한다("후라이드", 16000);
        상품을_생성한다("후라이드", 16000);

        List<Product> extract = 상품_목록을_조회한다();

        assertThat(extract).hasSize(3);
    }
}
