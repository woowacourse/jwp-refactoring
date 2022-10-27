package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public enum ProductFixtures {

    후라이드_상품(1L, "후라이드", 16000),
    양념치킨_상품(2L, "양념치킨", 16000),
    반반치킨_상품(3L, "반반치킨", 16000),
    통구이_상품(4L, "통구이", 16000),
    간장치킨_상품(5L, "간장치킨", 17000),
    순살치킨_상품(6L, "순살치킨", 17000),
    ;

    private final Long id;
    private final String name;
    private final int price;

    ProductFixtures(final Long id, final String name, final int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product 상품_생성(final String name, final int price) {
        return 상품_생성(name, BigDecimal.valueOf(price));
    }

    public static Product 상품_생성(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product 상품_생성(final Long id, final String name, final int price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static List<Product> 상품_목록_조회() {
        return Arrays.stream(ProductFixtures.values())
                .map(fixture -> 상품_생성(fixture.getId(), fixture.getName(), fixture.getPrice()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
