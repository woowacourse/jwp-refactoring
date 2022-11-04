package kitchenpos.fixture;

import kitchenpos.product.domain.entity.Product;
import kitchenpos.vo.Price;

public class ProductFixture {

    public static final Product PRODUCT_1 = new Product(1L, "후라이드", new Price(16000));
    public static final Product PRODUCT_2 = new Product(2L, "양념치킨", new Price(16000));
    public static final Product PRODUCT_3 = new Product(3L, "반반치킨", new Price(16000));
    public static final Product PRODUCT_4 = new Product(4L, "통구이", new Price(16000));
    public static final Product PRODUCT_5 = new Product(5L, "간장치킨", new Price(17000));
    public static final Product PRODUCT_6 = new Product(6L, "순살치킨", new Price(17000));
}
