package kitchenpos.application.fixture;

import kitchenpos.domain.Product;
import kitchenpos.ui.dto.CreateProductRequest;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ProductServiceFixture {

    protected CreateProductRequest 등록_요청_상품_dto;
    protected Product 등록한_상품;
    protected CreateProductRequest 가격이_입력되지_않은_상품;
    protected CreateProductRequest 가격이_0보다_작은_상품;
    protected List<Product> 저장된_모든_상품;

    @BeforeEach
    void setUp() {
        등록_요청_상품_dto = new CreateProductRequest("치킨", BigDecimal.valueOf(10_000));
        등록한_상품 = new Product(등록_요청_상품_dto.getName(), 등록_요청_상품_dto.getPrice());

        // 상품_가격이_입력되지_않은_경우_예외가_발생한다
        가격이_입력되지_않은_상품 = new CreateProductRequest("가격이 입력되지 않은 상품", null);

        // 상품_가격이_0_보다품_작은_경우_예외가_발생한다
        가격이_0보다_작은_상품 = new CreateProductRequest("가격이 0보다 작은 상품", BigDecimal.valueOf(-1));

        // 모든_상품을_조회한다
        final Product 상품1 = new Product();
        final Product 상품2 = new Product();
        저장된_모든_상품 = List.of(상품1, 상품2);
    }
}
