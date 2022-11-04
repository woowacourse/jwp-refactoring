package kitchenpos.application;

import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static kitchenpos.support.fixture.dto.ProductDtoFixture.상품_생성_요청;
import static kitchenpos.support.fixture.dto.ProductDtoFixture.상품_생성_응답;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.infrastructure.JdbcTemplateProductDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("상품을 생성한다.")
        void success() {
            Product product = APPLE_1000.getProduct();

            ProductResponse actual = productService.create(상품_생성_요청(product));

            ProductResponse expected = 상품_생성_응답(product);
            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            jdbcTemplateProductDao.save(APPLE_1000.getProduct());
        }

        @Test
        @DisplayName("상품 전체 목록을 조회한다.")
        void success() {
            List<ProductResponse> responses = productService.list();

            assertThat(responses).hasSize(2);
        }
    }
}
