package kitchenpos.support.fixture;

import java.math.BigDecimal;
import kitchenpos.product.application.dto.ProductDto;
import kitchenpos.product.domain.Product;
import kitchenpos.vo.Price;

public class ProductFixture {

    public static Product 후라이드() {
        return new Product(
            "후라이드",
            new Price(BigDecimal.valueOf(16000))
        );
    }

    public static Product 양념치킨() {
        return new Product(
            "양념치킨",
            new Price(BigDecimal.valueOf(16000))
        );
    }

    public static Product 반반치킨() {
        return new Product(
            "반반치킨",
            new Price(BigDecimal.valueOf(16000))
        );
    }

    public static Product 통구이() {
        return new Product(
            "통구이",
            new Price(BigDecimal.valueOf(16000))
        );
    }

    public static Product 간장치킨() {
        return new Product(
            "간장치킨",
            new Price(BigDecimal.valueOf(16000))
        );
    }

    public static Product 순살치킨() {
        return new Product(
            "순살치킨",
            new Price(BigDecimal.valueOf(16000))
        );
    }

    public static ProductDto 후라이드_DTO() {
        return new ProductDto(
            null,
            "후라이드",
            BigDecimal.valueOf(16000)
        );
    }

    public static ProductDto 양념치킨_DTO() {
        return new ProductDto(
            null,
            "양념치킨",
            BigDecimal.valueOf(16000)
        );
    }

    public static ProductDto 반반치킨_DTO() {
        return new ProductDto(
            null,
            "반반치킨",
            BigDecimal.valueOf(16000)
        );
    }

    public static ProductDto 통구이_DTO() {
        return new ProductDto(
            null,
            "통구이",
            BigDecimal.valueOf(16000)
        );
    }

    public static ProductDto 간장치킨_DTO() {
        return new ProductDto(
            null,
            "간장치킨",
            BigDecimal.valueOf(16000)
        );
    }

    public static ProductDto 순살치킨_DTO() {
        return new ProductDto(
            null,
            "순살치킨",
            BigDecimal.valueOf(16000)
        );
    }
}
