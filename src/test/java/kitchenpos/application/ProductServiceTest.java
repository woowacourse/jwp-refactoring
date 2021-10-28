package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        Product product = productConstructor("후라이드 치킨", 15000);
        Product expected = productConstructor(1L, "후라이드 치킨", 15000);
        given(productDao.save(product)).willReturn(expected);

        //when
        Product actual = productService.create(product);

        //then
        assertEquals(actual, expected);
    }

    @DisplayName("상품 가격이 null 이면 예외가 발생한다.")
    @Test
    void createWhenInputNullPrice() {
        //given
        Product product = productConstructor(null, "후라이드 치킨", null);

        //then
        assertThatThrownBy(() -> productService.create(product))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 음수면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, -1})
    void createWhenInputLessThanZeroPrice(final int price) {
        //given
        Product product = productConstructor(null, "후라이드 치킨", price);

        //then
        assertThatThrownBy(() -> productService.create(product))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void readAll() {
        //given
        List<Product> expected = Arrays.asList(
            productConstructor(1L, "양념 치킨", 16000),
            productConstructor(2L, "간장 치킨", 17000),
            productConstructor(3L, "깐풍 치킨", 20000),
            productConstructor(4L, "마늘 치킨", 18000)
        );
        given(productDao.findAll()).willReturn(expected);

        //when
        List<Product> actual = productService.list();

        //then
        assertEquals(actual, expected);
    }

    private Product productConstructor(final String name, final int price) {
        return productConstructor(null, name, price);
    }

    private Product productConstructor(final Long id, final String name, final int price) {
        return productConstructor(id, name, new BigDecimal(price));
    }

    private Product productConstructor(final Long id, final String name, final BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
