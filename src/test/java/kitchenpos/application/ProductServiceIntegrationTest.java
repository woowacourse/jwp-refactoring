package kitchenpos.application;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import kitchenpos.domain.Product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품_등록_성공() {
        int currentSize = productService.list().size();

        Product created = 상품_등록("치킨", 18000);

        assertThat(created.getId()).isEqualTo(currentSize + 1);
    }

    @Test
    void 상품_이름이_없는_경우_예외_발생() {
        assertThatThrownBy(() -> 상품_등록("치킨", -1000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_0_이상이_아닌_경우_예외_발생() {
        assertThatThrownBy(() -> 상품_등록("치킨", -1000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Product 상품_등록(String name, int price) {
        Product product = new Product(name, new BigDecimal(price));
        Product created = productService.create(product);
        return created;
    }
}
