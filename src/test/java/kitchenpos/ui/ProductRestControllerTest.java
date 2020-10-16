package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.utils.TestFixture;

class ProductRestControllerTest extends ControllerTest {
    @Autowired
    private ProductDao productDao;

    @DisplayName("create: 상품을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        final Product product = TestFixture.getProduct(16000);
        create("/api/products", product)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("후라이드 치킨"));
    }

    @DisplayName("findProducts: 상품 목록을 조회할 수 있다.")
    @Test
    void findProductsTest() throws Exception {
        final Product friedChicken = new Product("후라이드", BigDecimal.valueOf(16000));
        final Product seasonedChicken = new Product("양념치킨", BigDecimal.valueOf(16000));

        productDao.save(friedChicken);
        productDao.save(seasonedChicken);

        findList("/api/products")
                .andExpect(jsonPath("$[0].name").value("후라이드"))
                .andExpect(jsonPath("$[1].name").value("양념치킨"));
    }
}
