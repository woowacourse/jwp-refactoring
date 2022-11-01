package kitchenpos.support.fixture;

import java.math.BigDecimal;

public enum ProductFixtures {

    후라이드상품(1L, "후라이드", 16000),
    양념치킨상품(2L, "앙념치킨", 16000),
    반반치킨상품(3L, "반반치킨", 16000)
    ;

    private final long id;
    private final String name;
    private final int price;

    ProductFixtures(final long id, final String name, final int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static long 존재하지_않는_상품_ID() {
        return -1L;
    }

    public static BigDecimal 상품_가격_음수() {
        return BigDecimal.valueOf(-1);
    }

    public static BigDecimal 상품_가격_NULL() {
        return null;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
