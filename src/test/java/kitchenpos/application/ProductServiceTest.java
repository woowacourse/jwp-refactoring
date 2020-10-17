package kitchenpos.application;

import kitchenpos.config.Dataset;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao dao;

    @InjectMocks
    private ProductService service;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = Dataset.product_포테이토_피자();
    }

    @DisplayName("상품 생성 실패 - 가격 null일 때")
    @Test
    public void createFailPriceNull() {
        product.setPrice(null);

        assertThatThrownBy(() -> service.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 실패 - 가격 음수일 때")
    @Test
    public void createFailPriceNegative() {
        product.setPrice(BigDecimal.valueOf(-10L));

        assertThatThrownBy(() -> service.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성")
    @Test
    public void createProduct() {
        given(dao.save(any(Product.class))).willReturn(product);

        final Product savedProduct = service.create(this.product);

        assertThat(savedProduct.getId()).isEqualTo(5L);
        assertThat(savedProduct.getName()).isEqualTo("포테이토 피자");
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(12000L));
    }
}
