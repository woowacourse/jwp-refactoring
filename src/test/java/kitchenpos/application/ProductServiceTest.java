package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

class ProductServiceTest extends ServiceTest {

    @Test
    void create() {
        // given
        String name = "후라이드";
        Product product = getProduct(1L, name, BigDecimal.valueOf(16000));

        given(productDao.save(any()))
                .willReturn(product);

        // when
        Product actual = productService.create(product);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(name)
        );
    }

    @Test
    void list() {
        // given
        Product product1 = getProduct(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = getProduct(2L, "양념치킨", BigDecimal.valueOf(16000));

        given(productDao.findAll())
                .willReturn(List.of(product1, product2));

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
