package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ServiceTest
class ProductServiceTest {

    @Mock
    private ProductDao mockProductDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성")
    @Nested
    class CreateProduct {

        @BeforeEach
        void setUp() {
            when(mockProductDao.save(any())).then(AdditionalAnswers.returnsFirstArg());
        }

        @DisplayName("상품을 생성한다.")
        @Test
        void create() {
            Product product = createProduct();
            Product savedProduct = productService.create(product);
            assertThat(savedProduct).isEqualTo(product);
        }

        @DisplayName("상품의 가격은 음수일 수 없다.")
        @Test
        void createWithInvalidPrice() {
            Product product = createProduct(BigDecimal.valueOf(-1L));
            assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 리스트를 반환한다.")
    @Test
    void list() {
        Product product = createProduct();
        when(mockProductDao.findAll()).thenReturn(Collections.singletonList(product));
        List<Product> list = productService.list();
        assertAll(
                () -> assertThat(list).hasSize(1),
                () -> assertThat(list).contains(product)
        );
    }
}
