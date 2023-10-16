package kitchenpos.application;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        // given
        final Product product = new Product();
        product.setPrice(BigDecimal.TEN);
        product.setName("상품");

        given(productDao.save(any()))
            .willReturn(new Product() {{
                setId(1L);
                setPrice(BigDecimal.TEN);
                setName("상품");
            }});

        // when
        final Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @DisplayName("상품의 가격이 null 이면 예외가 발생한다.")
    @Test
    void create_failNullPrice() {
        // given
        final Product product = new Product();
        product.setPrice(null);

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수이면 예외가 발생한다.")
    @Test
    void create_failNegativePrice() {
        // given
        final Product product = new Product();
        product.setPrice(BigDecimal.valueOf(-1L));

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트를 조회한다.")
    @Test
    void list() {
        // given
        given(productDao.findAll())
            .willReturn(List.of(
                new Product() {{
                    setId(1L);
                    setPrice(BigDecimal.TEN);
                    setName("상품1");
                }},
                new Product() {{
                    setId(2L);
                    setPrice(BigDecimal.valueOf(1000L));
                    setName("상품2");
                }}
            ));

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(2);
        assertThat(products.stream()
                       .map(Product::getId)
                       .collect(toList())).usingRecursiveComparison()
            .isEqualTo(List.of(1L, 2L));
    }
}
