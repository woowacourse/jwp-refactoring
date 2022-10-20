package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        ProductCreateRequest request = new ProductCreateRequest("name", new BigDecimal(1000));

        Long savedId = productService.create(request);

        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("Product 생성 시, 가격이 0원 이하일 때 예외가 발생한다.")
    void createThrowException() {
        ProductCreateRequest request = new ProductCreateRequest("name", new BigDecimal(-10));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(6);
    }
}
