package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
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

    @DisplayName("상품을 추가한다.")
    @Test
    void create() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(1000L));

        Product saved = new Product();
        saved.setId(1L);
        saved.setName(product.getName());
        saved.setPrice(product.getPrice());

        given(productDao.save(any(Product.class))).willReturn(saved);

        Product actual = productService.create(product);

        assertAll(
            () -> assertThat(actual).extracting(Product::getId).isEqualTo(saved.getId()),
            () -> assertThat(actual).extracting(Product::getName).isEqualTo(saved.getName()),
            () -> assertThat(actual).extracting(Product::getPrice).isEqualTo(saved.getPrice())
        );
    }

    @DisplayName("상품을 추가할 시 가격이 null일 경우 예외 처리한다.")
    @Test
    void createWithNullPrice() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(null);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 추가할 시 가격이 음수일 경우 예외 처리한다.")
    @Test
    void createWithNegativePrice() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(-10L));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체 목록을 조회한다.")
    @Test
    void list() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(1_000L));

        given(productService.list()).willReturn(Collections.singletonList(product));

        List<Product> actual = productService.list();

        assertThat(actual).hasSize(1);
    }
}