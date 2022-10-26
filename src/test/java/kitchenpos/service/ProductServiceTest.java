package kitchenpos.service;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.util.FakeProductDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductServiceTest {

    private final ProductDao productDao = new FakeProductDao();
    private final ProductService productService = new ProductService(productDao);
    @DisplayName("제품을 생성한다")
    @Test
    void create() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("샐러드", 3000L);

        Product product = productService.create(productCreateRequest);

        assertAll(
                () -> assertThat(product.getName()).isEqualTo("샐러드"),
                () -> assertThat(product.getPrice()).isEqualTo(3000L)
        );
    }
    @DisplayName("제품 가격이 null인 제품을 생성할 수 없다")
    @Test
    void create_priceNull() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("샐러드", null);

        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("제품 가격이 음수인 제품을 생성할 수 없다")
    @Test
    void create_priceNegative() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("샐러드", -1L);

        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("제품 목록을 조회한다")
    @Test
    void list() {
        productDao.save(new Product("test1", 1L));
        productDao.save(new Product("test2", 1L));
        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(2);
    }
}
