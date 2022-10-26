package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        ProductCreateRequest productCreateRequest =
                new ProductCreateRequest("뿌링 치킨", BigDecimal.valueOf(10_000L));
        Product savedProduct = productService.create(productCreateRequest);

        assertThat(productDao.findById(savedProduct.getId())).isPresent();
    }

    @Test
    void 상품목록을_불러온다() {
        int beforeSize = productService.list().size();
        productDao.save(new Product("뿌링 치킨", BigDecimal.valueOf(10_000L)));

        assertThat(productService.list().size()).isEqualTo(beforeSize + 1);
    }
}
