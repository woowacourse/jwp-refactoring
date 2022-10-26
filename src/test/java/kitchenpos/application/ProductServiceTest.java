package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCommand;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("제품 관련 기능에서")
@ExtendWith(DataClearExtension.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("상품을 생성할 때")
    class CreateProduct {

        @Test
        @DisplayName("상품이 정상적으로 생성된다.")
        void create() {
            ProductCommand product = givenProduct("강정치킨", 18000);

            ProductResponse response = productService.create(product);

            assertThat(response.getId()).isNotNull();
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @ParameterizedTest(name = "가격이 0원 미만이면 예외가 발생한다.")
            @ValueSource(ints = {-1, -1000})
            void createPriceNull(int price) {
                ProductCommand product = givenProduct(price);

                assertThatThrownBy(() -> productService.create(product))
                        .hasMessage("가격은 0원 이상이어야 합니다.");
            }
        }
    }

    @Test
    @DisplayName("존재하는 상품을 모두 조회한다.")
    void list() {
        productRepository.save(new Product("릭냥이네 치킨", 19000));
        productRepository.save(new Product("호호네 치킨", 19000));
        productRepository.save(new Product("제이슨 치킨", 19000));

        List<ProductResponse> responses = productService.list();

        assertThat(responses).hasSize(3);
    }

    private ProductCommand givenProduct(String name, int price) {
        return new ProductCommand(name, BigDecimal.valueOf(price));
    }

    private ProductCommand givenProduct(int price) {
        return givenProduct("강정치킨", price);
    }

    private ProductCommand givenProduct(String name) {
        return givenProduct(name, 30000);
    }
}
