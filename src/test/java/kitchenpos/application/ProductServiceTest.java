package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        final String name = "치킨";
        final BigDecimal price = BigDecimal.valueOf(10000);
        final ProductCreateRequest request = new ProductCreateRequest(name, price);

        final ProductResponse response = productService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(name),
                () -> assertThat(response.getPrice().longValue()).isEqualTo(price.longValue())
        );
    }

    @DisplayName("등록된 상품들을 조회할 수 있다.")
    @Test
    void list() {
        productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        productDao.save(new Product("피자", BigDecimal.valueOf(8000)));

        final List<ProductResponse> response = productService.list();

        assertThat(response).hasSize(2);
    }
}
