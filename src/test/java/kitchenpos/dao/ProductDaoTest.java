package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품_엔티티를_저장한다() {
        Product productEntity = createProductEntity();

        Product saveProduct = productDao.save(productEntity);

        assertThat(saveProduct.getId()).isPositive();
    }

    @Test
    void 상품_엔티티를_조회한다() {
        Product productEntity = createProductEntity();
        Product saveProduct = productDao.save(productEntity);

        assertThat(productDao.findById(saveProduct.getId())).isPresent();
    }

    @Test
    void 모든_상품_엔티티를_조회한다() {
        Product productEntityA = createProductEntity();
        Product productEntityB = createProductEntity();
        Product saveProductA = productDao.save(productEntityA);
        Product saveProductB = productDao.save(productEntityB);

        List<Product> products = productDao.findAll();

        assertThat(products)
                .usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveProductA, saveProductB);
    }

    private Product createProductEntity() {
        Product product = new Product();
        product.setName("gray");
        product.setPrice(BigDecimal.valueOf(1_000_000_000));
        return product;
    }
}
