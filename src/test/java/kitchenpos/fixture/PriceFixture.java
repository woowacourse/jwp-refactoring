package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.common.Price;

public final class PriceFixture {

    public static Price 가격_10원_생성() {
        return new Price(BigDecimal.TEN);
    }

    private PriceFixture() {
    }
}
