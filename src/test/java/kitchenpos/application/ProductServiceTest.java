package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService sut;

    @Test
    void throwException_WhenPriceNull() {
        // given
        Product product = new Product();
        product.setName("강정치킨");

        // when && then
        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwException_WhenPriceNegative() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(-1L));

        // when && then
        assertThatThrownBy(() -> sut.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delegateSaveAndReturnSavedEntity() {
        // given
        Product expected = new Product();
        expected.setId(1L);
        expected.setName("강정치킨");
        expected.setPrice(BigDecimal.valueOf(1000L));

        given(productDao.save(expected)).willReturn(expected);

        // when
        Product actual = sut.create(expected);

        // then
        assertThat(actual).isEqualTo(expected);
        verify(productDao, times(1)).save(expected);
    }

    @Test
    void returnAllSavedEntities() {
        // given
        ArrayList<Product> expected = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(1000L));
        expected.add(product);

        given(productDao.findAll()).willReturn(expected);

        // when
        List<Product> actual = sut.list();

        // then
        assertThat(actual).isEqualTo(expected);
        verify(productDao, times(1)).findAll();
    }
}
