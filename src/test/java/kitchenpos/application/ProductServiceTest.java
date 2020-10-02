package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("create: 상품 등록 확인 테스트")
    @Test
    void createTest() {
        final Product product = new Product();
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(16000));
        when(productDao.save(any())).thenReturn(product);
        final Product actual = productService.create(product);

        assertThat(actual.getName()).isEqualTo(product.getName());
        assertThat(actual.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("findProducts: 상품 전체 목록 조회 확인 테스트")
    @Test
    void findProductsTest() {
        final Product friedChicken = new Product();
        friedChicken.setName("후라이드");
        friedChicken.setPrice(BigDecimal.valueOf(160000));
        final Product seasonedChicken = new Product();
        seasonedChicken.setName("양념 치킨");
        seasonedChicken.setPrice(BigDecimal.valueOf(160000));
        when(productDao.findAll()).thenReturn(Arrays.asList(friedChicken, seasonedChicken));

        final List<Product> products = productService.list();

        assertThat(products).hasSize(2);
    }
}
