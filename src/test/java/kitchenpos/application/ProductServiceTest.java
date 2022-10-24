package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.상품_등록;
import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @DisplayName("상품의 목록을 올바르게 조회한다.")
    @Test
    void findAllProduct() {
        // given
        productDao.save(후라이드_치킨());
        productDao.save(양념_치킨());

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }

    @DisplayName("상품을 정상적으로 등록한다.")
    @Test
    void createProduct() {
        // given
        Product chicken = 후라이드_치킨();

        // when
        Product savedChicken = productService.create(chicken);

        // then
        List<Product> products = productDao.findAll();
        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(savedChicken.getName()).isEqualTo(chicken.getName()),
                () -> assertThat(savedChicken.getPrice()).isEqualByComparingTo(chicken.getPrice())
        );
    }

    @DisplayName("상품 등록 시 상품의 가격이 비어있으면 예외를 발생한다.")
    @Test
    void createPriceNullProduct() {
        // given
        Product chicken = new Product();
        chicken.setName("후라이드치킨");
        chicken.setPrice(null);

        // when & then
        assertThatThrownBy(
                () -> productService.create(chicken)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 시 상품의 가격이 비어있으면 예외를 발생한다.")
    @Test
    void createPrice0Product() {
        // given
        Product chicken = 상품_등록("페퍼로니피자", -1000);

        // when & then
        assertThatThrownBy(
                () -> productService.create(chicken)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
