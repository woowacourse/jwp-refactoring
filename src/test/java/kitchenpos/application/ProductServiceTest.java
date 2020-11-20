package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.exception.InvalidProductPriceException;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.ProductRepository;

@SpringBootTest
@Sql("classpath:truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("정상적으로 Product를 생성한다.")
    @Test
    void create() {
        ProductCreateRequest request = ProductFixture.createRequestPriceOf(
            ProductFixture.DEFAULT_PRICE.longValue());
        Product productWithoutId = ProductFixture.createWithoutId();
        Product productWithId = ProductFixture.createWithId(1L);

        ProductResponse savedProduct = productService.create(request);
        assertThat(savedProduct)
            .isEqualToIgnoringGivenFields(productWithoutId, "id");
        assertThat(savedProduct).extracting(ProductResponse::getId)
            .isEqualTo(productWithId.getId());
    }

    @DisplayName("가격이 null인 Product 생성 요청시 예외를 반환한다.")
    @Test
    void createIllegalPriceProduct() {
        ProductCreateRequest request = ProductFixture.createRequestPriceOf(null);

        assertThatThrownBy(() -> productService.create(request))
            .isInstanceOf(InvalidProductPriceException.class);
    }

    @DisplayName("가격이 음수인 Product 생성 요청시 예외를 반환한다.")
    @Test
    void createNegativePriceProduct() {
        ProductCreateRequest request = ProductFixture.createRequestPriceOf(-100L);

        assertThatThrownBy(() -> productService.create(request))
            .isInstanceOf(InvalidProductPriceException.class);
    }

    @DisplayName("정상적으로 저장된 Product를 불러온다.")
    @Test
    void list() {
        Product product1 = ProductFixture.createWithId(1L);
        Product product2 = ProductFixture.createWithId(2L);

        productRepository.saveAll(Arrays.asList(product1, product2));

        assertThat(productService.list())
            .usingRecursiveFieldByFieldElementComparator()
            .usingComparatorForType(Comparator.comparingLong(BigDecimal::longValue),
                BigDecimal.class)
            .isEqualTo(Arrays.asList(product1, product2));
    }
}
