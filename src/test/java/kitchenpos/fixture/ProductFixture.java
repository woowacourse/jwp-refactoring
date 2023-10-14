package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후라이드() {
        return new Product(
            "후라이드",
            BigDecimal.valueOf(16000)
        );
    }

    public static Product 양념치킨() {
        return new Product(
            "양념치킨",
            BigDecimal.valueOf(16000)
        );
    }

    public static Product 반반치킨() {
        return new Product(
            "반반치킨",
            BigDecimal.valueOf(16000)
        );
    }

    public static Product 통구이() {
        return new Product(
            "통구이",
            BigDecimal.valueOf(16000)
        );
    }

    public static Product 간장치킨() {
        return new Product(
            "간장치킨",
            BigDecimal.valueOf(16000)
        );
    }

    public static Product 순살치킨() {
        return new Product(
            "순살치킨",
            BigDecimal.valueOf(16000)
        );
    }
}
