package kitchenpos.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private List<Product> standardProducts;
    private Product standardProduct;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        standardProduct = new Product();
        standardProduct.setId(1L);
        standardProduct.setName("신상품");
        standardProduct.setPrice(BigDecimal.valueOf(1000.00d));
        standardProducts = new LinkedList<>();
        standardProducts.add(standardProduct);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void getProducts() {
        //given
        given(productDao.findAll()).willReturn(standardProducts);

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products.size()).isEqualTo(1);
    }

    @DisplayName("상품을 추가한다.")
    @Test
    void createProduct() {
        //given
        Product product = new Product();
        Long id = 2L;
        String name = "신상품";
        BigDecimal price = BigDecimal.valueOf(1000.0d);
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        given(productDao.save(product)).willReturn(product);

        //when
        Product createdProduct = productService.create(product);

        //then
        assertAll(
            () -> assertThat(createdProduct.getId()).isEqualTo(2L),
            () -> assertThat(createdProduct.getPrice()).isEqualTo("1000.0"),
            () -> assertThat(createdProduct.getName()).isEqualTo(name)
        );
    }

    @DisplayName("가격이 0보단 커야 한다.")
    @Test
    void createProductWithMinusPrice() {
        //given
        Product product = new Product();
        Long id = 2L;
        String name = "신상품";
        BigDecimal price = BigDecimal.valueOf(-1000.0d);
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        //when

        //then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 비어서 들어와선 안된다.")
    @Test
    void createProductWithNullPrice() {
        //given
        Product product = new Product();
        Long id = 2L;
        String name = "신상품";
        product.setId(id);
        product.setName(name);

        //when

        //then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

}