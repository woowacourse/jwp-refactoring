package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.exception.EmptyDataException;
import kitchenpos.exception.LowerThanZeroPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    private static final String PRODUCE_NAME = "빅맥";
    private static final BigDecimal PRICE = new BigDecimal(5000);

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(PRODUCE_NAME, PRICE);

        productService.create(productCreateRequest);

        List<ProductResponse> products = productService.list();
        List<String> productNames = products.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toUnmodifiableList());
        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(productNames).contains(PRODUCE_NAME)
        );
    }

    @DisplayName("상품 가격이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void create_NullPrice() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(PRODUCE_NAME, null);

        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(EmptyDataException.class)
                .hasMessageContaining(Price.class.getSimpleName())
                .hasMessageContaining("입력되지 않았습니다.");
    }

    @DisplayName("상품 가격이 0보다 작으면 예외를 발생시킨다.")
    @Test
    void create_InvalidPrice() {
        BigDecimal invalidPrice = new BigDecimal(-1);
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(PRODUCE_NAME, invalidPrice);

        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(LowerThanZeroPriceException.class);
    }
}
