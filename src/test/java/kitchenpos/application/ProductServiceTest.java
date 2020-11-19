package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static kitchenpos.fixture.ProductFixture.createProductWithPrice;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("Product 생성 테스트")
    @Test
    void createOrderLineItems() {
        BigDecimal price = BigDecimal.ONE;
        Product product = createProductWithPrice(price);
        given(productRepository.save(product)).willReturn(createProduct(1L, price));

        Product actual = productService.create(product);
        Product expect = createProduct(1L, price);

        assertThat(actual).isEqualToComparingFieldByField(expect);
    }

    @DisplayName("product 전체 조회")
    @Test
    void list() {
        Product product = createProduct(1L, BigDecimal.ONE);
        given(productRepository.findAll()).willReturn(Arrays.asList(product));

        List<Product> actual = productService.list();
        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).isEqualToComparingFieldByField(product);
        });
    }
}