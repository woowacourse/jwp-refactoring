package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.request.ProductCreationRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Sql(value = "/initialization.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @ParameterizedTest(name = "상품 이름이 1글자 미만, 255글자 이상이면, 저장할 수 없다.")
    @ValueSource(ints = {0, 256})
    void createFailTest_ByProductNameLengthIsNotInRange(int count) {
        //given
        String name = "a".repeat(count);
        ProductCreationRequest request = new ProductCreationRequest(name, BigDecimal.TEN);

        //when then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 이름은 1글자 이상, 255자 이하여야 합니다.");
    }

    @DisplayName("상품 금액이 null이면, 저장할 수 없다.")
    @Test
    void createFailTest_ByProductPriceIsNull() {
        //given
        ProductCreationRequest request = new ProductCreationRequest("TestProduct", null);

        //when then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("상품 금액은 null일 수 없습니다.");
    }

    @ParameterizedTest(name = "상품 금액이 0원 미만이면, 저장할 수 없다.")
    @ValueSource(ints = {-1000, -1})
    void createFailTest_ByProductPriceIsLessThanZero(int price) {
        //given
        ProductCreationRequest request = new ProductCreationRequest("TestProduct", BigDecimal.valueOf(price));

        //when then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 금액은 0원 이상이어야 합니다.");
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createSuccessTest() {
        //given
        ProductCreationRequest request = new ProductCreationRequest("TestProduct", BigDecimal.TEN);

        //when
        ProductResponse response = productService.create(request);

        //then
        Product findProduct = productRepository.findById(response.getId()).get();
        ProductResponse expected = ProductResponse.from(findProduct);

        assertThat(findProduct).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void listSuccessTest() {
        //given
        ProductCreationRequest request = new ProductCreationRequest("TestProduct", BigDecimal.TEN);

        Long savedProductId = productService.create(request).getId();

        //when
        List<ProductResponse> responses = productService.list();
        Product findProduct = productRepository.findById(savedProductId).get();
        //then
        assertAll(
                () -> assertThat(responses).usingRecursiveComparison()
                        .ignoringFields("price")
                        .isEqualTo(List.of(findProduct)),
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.get(0).getPrice()).isEqualByComparingTo(findProduct.getPrice())
        );
    }

}
