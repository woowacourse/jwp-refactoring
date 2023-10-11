package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({ProductService.class, JdbcTemplateProductDao.class})
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        Product expected = new Product();
        expected.setName("상품");
        expected.setPrice(BigDecimal.valueOf(1000));

        // when
        Product actual = productService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(productDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
        });
    }

    @DisplayName("상품 등록 시 상품 가격이 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = { -1L, -100L, -1000L })
    void create_FailWithNegativePrice(long invalidPrice) {
        // given
        Product invalidProduct = new Product();
        invalidProduct.setName("상품");
        invalidProduct.setPrice(BigDecimal.valueOf(invalidPrice));

        // when & then
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 시 상품 가격이 null인 경우 예외가 발생한다.")
    @Test
    void create_FailWithNullablePrice() {
        // given
        Product invalidProduct = new Product();
        invalidProduct.setName("상품");
        invalidProduct.setPrice(null);

        // when & then
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Product 후라이드 = new Product();
        후라이드.setName("후라이드");
        후라이드.setPrice(BigDecimal.valueOf(16000));

        Product 양념치킨 = new Product();
        양념치킨.setName("양념치킨");
        양념치킨.setPrice(BigDecimal.valueOf(16000));

        productDao.save(후라이드);
        productDao.save(양념치킨);
        
        // when
        List<Product> list = productService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).extracting("name")
                    .contains(후라이드.getName(), 양념치킨.getName());
        });
    }
}
