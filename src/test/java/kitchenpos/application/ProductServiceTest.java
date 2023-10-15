package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.support.FixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("새로운 상품을 생성한다.")
    @Test
    void create_new_product() {
        // given
        final Product newProduct = FixtureFactory.forSaveProduct("새 상품", new BigDecimal(1000));
        final Product savedProduct = FixtureFactory.savedProduct(1L, "새 상품", new BigDecimal(1000));

        given(productDao.save(newProduct))
                .willReturn(savedProduct);

        // when
        final Product result = productService.create(newProduct);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedProduct.getId());
            softly.assertThat(result.getName()).isEqualTo(savedProduct.getName());
            softly.assertThat(result.getPrice()).isEqualTo(savedProduct.getPrice());
        });
    }

    @DisplayName("새 상품의 값이 음수이면 예외를 발생한다.")
    @Test
    void create_fail_new_product() {
        // given
        final Product wrongProduct = FixtureFactory.forSaveProduct("새 상품", new BigDecimal(-100));

        // when
        // then
        assertThatThrownBy(() -> productService.create(wrongProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새 상품의 값이 null이면 예외를 발생한다.")
    @Test
    void create_fail_with_product_price_null() {
        // given
        final Product wrongProduct = FixtureFactory.forSaveProduct("새 상품", null);

        // when
        // then
        assertThatThrownBy(() -> productService.create(wrongProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전제 정보를 불러온다.")
    @Test
    void find_all_products() {
        // given
        final Product product1 = FixtureFactory.savedProduct(1L, "새 상품1", new BigDecimal(500));
        final Product product2 = FixtureFactory.savedProduct(2L, "새 상품2", new BigDecimal(1000));

        final List<Product> products = List.of(product1, product2);

        given(productDao.findAll())
                .willReturn(products);

        // when
        final List<Product> result = productService.list();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(products);
    }
}
