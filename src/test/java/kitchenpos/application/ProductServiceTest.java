package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    }

    @Test
    @DisplayName("상품을 만드는 테스트")
    void create() {
        // Given
        given(productDao.save(product)).willReturn(product);

        // When
        Product savedProduct = productService.create(product);

        // Then
        then(productDao).should().save(product);
        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("상품을 불러오는 테스트")
    void list() {
        // Given
        given(productDao.findAll()).willReturn(List.of(product));

        // When
        List<Product> products = productService.list();

        // Then
        then(productDao).should().findAll();
        assertThat(products).hasSize(1);
        assertThat(products).contains(product);
    }
}
