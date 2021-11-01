package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("ProductService를 테스트한다.")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        menuProductDao.deleteAll();
        productDao.deleteAll();
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        Product product = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));

        //when
        Product result = productService.create(product);

        //then
        assertThat(result.getName()).isEqualTo("강정치킨");
        assertThat(result.getPrice().toBigInteger()).isEqualTo(product.getPrice().toBigInteger());
    }

    @DisplayName("상품 등록시 가격이 0원 미만이면 예외를 던진다.")
    @Test
    void create_price_exception() {
        Product product = new Product(1L, "강정치킨", BigDecimal.valueOf(-17000));

        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        productService.create(new Product(1L, "강정치킨", BigDecimal.valueOf(17000)));
        productService.create(new Product(2L, "양념치킨", BigDecimal.valueOf(17000)));
        productService.create(new Product(3L, "후라이드치킨", BigDecimal.valueOf(15000)));

        //when
        List<Product> result = productService.list();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

}