package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductDao productDao;

    @DisplayName("상품을 등록한다")
    @Test
    void create() {
        Product product = new Product();
        product.setName("콜라");
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(2000L));

        given(productDao.save(any(Product.class))).willReturn(product);
        Product savedProduct = productService.create(product);

        assertAll(
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo("콜라"),
            () -> assertThat(savedProduct.getPrice().longValue()).isEqualTo(2000L)
        );
    }

    @DisplayName("상품의 가격이 음수나 Null인 경우 예외가 발생하는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"-1,"})
    void createInvalidProduct(Long price) {
        Product product = new Product();
        product.setName("콜라");
        product.setPrice(BigDecimal.valueOf(price));
        product.setId(1L);

        assertThatThrownBy(
            () -> productService.create(product)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void list() {
        Product product = new Product();
        product.setName("콜라");
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(2000L));

        given(productDao.findAll()).willReturn(Arrays.asList(product));
        List<Product> products = productService.list();

        assertAll(
            () -> assertThat(products).hasSize(1),
            () -> assertThat(products.get(0).getName()).isEqualTo("콜라"),
            () -> assertThat(products.get(0).getPrice().longValue()).isEqualTo(2000L)
        );
    }
}
