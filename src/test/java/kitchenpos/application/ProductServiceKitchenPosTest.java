package kitchenpos.application;

import kitchenpos.KitchenPosTestFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceKitchenPosTest extends KitchenPosTestFixture {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 강정치킨;
    private Product 튀김소보로;

    @BeforeEach
    void setUp() {
        강정치킨 = 상품을_저장한다(null, "강정치킨", BigDecimal.valueOf(1700));
        튀김소보로 = 상품을_저장한다(null, "튀김소보로", BigDecimal.valueOf(1200));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        Product product = 상품을_저장한다(1L, "강정치킨", BigDecimal.valueOf(1700));
        given(productDao.save(any(Product.class))).willReturn(product);

        // when
        Product savedProduct = productService.create(강정치킨);

        // then
        assertThat(savedProduct).usingRecursiveComparison().isEqualTo(product);
        verify(productDao, times(1)).save(강정치킨);
    }

    @DisplayName("1자 이상의 문자로 구성된 상품명을 등록한다.")
    @Test
    void validateProductNameLength() {
        // given
        Product nullNameProduct = 상품을_저장한다(null, null, BigDecimal.valueOf(1700));
        Product emptyNameProduct = 상품을_저장한다(null, "", BigDecimal.valueOf(1700));

        // then
        assertThatThrownBy(() -> productService.create(nullNameProduct)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(emptyNameProduct)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격의 상품은 0원 이상이어야한다.")
    @Test
    void validateProductPrice() {
        // given
        Product zeroPriceProduct = 상품을_저장한다(null, "강정치킨", BigDecimal.valueOf(-1));
        Product nullPriceProduct = 상품을_저장한다(null, "강정치킨", null);

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
        verify(productDao, times(1)).findAll();
    }
}