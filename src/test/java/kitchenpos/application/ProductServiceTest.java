package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static kitchenpos.fixture.ProductFixture.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends AbstractServiceTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("상품 가격이 0원 이상인 경우 상품을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, 1L})
    void create(Long price) {
        ProductCreateRequest productCreateRequest = createProductRequest("치킨", price);

        ProductResponse savedProduct = productService.create(productCreateRequest);

        assertAll(
            () -> assertThat(savedProduct).isNotNull(),
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo(productCreateRequest.getName()),
            () -> assertThat(savedProduct.getPrice().longValue())
                .isEqualTo(savedProduct.getPrice().longValue())
        );
    }


    @DisplayName("상품 가격이 null 인 경우 상품을 생성할 수 없다.")
    @Test
    void create_throws_exception() {
        ProductCreateRequest productCreateRequest = createProductRequest("치킨", null);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> productService.create(productCreateRequest));
    }


    @DisplayName("상품 가격이 0원 미만인 경우 상품을 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L})
    void create_throws_exception(Long price) {
        ProductCreateRequest productCreateRequest = createProductRequest("치킨", price);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> productService.create(productCreateRequest));
    }


    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<ProductResponse> savedProducts = ProductResponse.listOf(Arrays.asList(
            productRepository.save(createProduct(null, "치킨1", 10000L)),
            productRepository.save(createProduct(null, "치킨2", 10000L)),
            productRepository.save(createProduct(null, "치킨3", 10000L))
        ));

        List<ProductResponse> allProducts = productService.list();

        assertThat(allProducts).usingFieldByFieldElementComparator()
            .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
            .containsAll(savedProducts);
    }
}
