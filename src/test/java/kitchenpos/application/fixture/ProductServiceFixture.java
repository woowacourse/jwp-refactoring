package kitchenpos.application.fixture;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ProductServiceFixture {

    protected Product 저장된_상품;
    protected Product 저장된_상품1;
    protected Product 저장된_상품2;
    protected String 상품_이름 = "상품";
    protected BigDecimal 상품_가격 = BigDecimal.valueOf(1_000);
    protected BigDecimal 상품_가격이_음수 = BigDecimal.valueOf(-1_000);
    protected List<Product> 저장된_상품들;

    @BeforeEach
    void setUp() {
        저장된_상품 = new Product(상품_이름, 상품_가격);
        저장된_상품.updateId(1L);
        저장된_상품1 = 저장된_상품;

        저장된_상품2 = new Product("상품2", BigDecimal.valueOf(1_000));
        저장된_상품2.updateId(2L);

        저장된_상품들 = List.of(저장된_상품1, 저장된_상품2);
    }
}
