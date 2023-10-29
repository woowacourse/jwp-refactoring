package kitchenpos.application.fixture;

import kitchenpos.product.domain.Product;
import kitchenpos.common.vo.Price;
import kitchenpos.product.persistence.JpaProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ProductServiceFixture {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    protected List<Product> 저장된_모든_상품;

    @BeforeEach
    void setUp() {
        final Product 상품1 = new Product("상품1", new Price(BigDecimal.valueOf(10_000)));
        final Product 상품2 = new Product("상품2", new Price(BigDecimal.valueOf(10_000)));
        저장된_모든_상품 = List.of(상품1, 상품2);
    }

    protected void 모든_상품을_조회한다_픽스처_생성() {
        final Product 치킨 = new Product("치킨", new Price(BigDecimal.valueOf(10_000)));
        final Product 피자 = new Product("피자", new Price(BigDecimal.valueOf(10_000)));
        final Product 마라탕 = new Product("마라탕", new Price(BigDecimal.valueOf(10_000)));

        jpaProductRepository.saveAll(List.of(치킨, 피자, 마라탕));
    }
}
