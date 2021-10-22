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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 강정치킨;
    private Product 튀김소보로;

    @BeforeEach
    void setUp() {
        강정치킨 = new Product();
        강정치킨.setId(1L);
        강정치킨.setName("강정치킨");
        강정치킨.setPrice(BigDecimal.valueOf(1700));

        튀김소보로 = new Product();
        튀김소보로.setId(2L);
        튀김소보로.setName("튀김소보로");
        튀김소보로.setPrice(BigDecimal.valueOf(1200));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(1700));

        given(productDao.save(product)).willReturn(강정치킨);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isEqualTo(강정치킨);
    }

    @DisplayName("1자 이상의 문자로 구성된 상품명을 등록한다.")
    @Test
    void validateProductNameLength() {
        // given
        Product nullNameProduct = new Product();
        nullNameProduct.setName(null);
        nullNameProduct.setPrice(BigDecimal.valueOf(1700));

        Product emptyNameProduct = new Product();
        emptyNameProduct.setName("");
        emptyNameProduct.setPrice(BigDecimal.valueOf(1700));

        // then
        assertThatThrownBy(() -> productService.create(nullNameProduct)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(emptyNameProduct)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격의 상품은 0원 이상이어야한다.")
    @Test
    void validateProductPrice() {
        // given
        Product zeroPriceProduct = new Product();
        zeroPriceProduct.setName("강정치킨");
        zeroPriceProduct.setPrice(BigDecimal.valueOf(-1));

        Product nullPriceProduct = new Product();
        nullPriceProduct.setName("강정치킨");
        nullPriceProduct.setPrice(null);

        // when
        assertThatThrownBy(() -> productService.create(zeroPriceProduct)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(nullPriceProduct)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<Product> products = Arrays.asList(강정치킨, 튀김소보로);
        given(productDao.findAll()).willReturn(products);

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).containsExactly(강정치킨, 튀김소보로);
    }
}