package kitchenpos.dao;

import static kitchenpos.constants.DaoConstants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.DaoConstants.TEST_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductDaoTest extends KitchenPosDaoTest {

    @DisplayName("Product 저장 - 성공")
    @Test
    void save_Success() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);

        Product savedProduct = productDao.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(TEST_PRODUCT_NAME);
        assertThat(savedProduct.getPrice()).isEqualByComparingTo(TEST_PRODUCT_PRICE);
    }

    @DisplayName("Product ID로 Product 조회 - 조회됨, ID가 존재하는 경우")
    @Test
    void findById_ExistsId_ReturnProduct() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);
        Product savedProduct = productDao.save(product);

        Product foundProduct = productDao.findById(savedProduct.getId())
            .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 없습니다."));

        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getName()).isEqualTo(savedProduct.getName());
        assertThat(foundProduct.getPrice()).isEqualByComparingTo(savedProduct.getPrice());
    }

    @DisplayName("Product ID로 Product 조회 - 조회되지 않음, ID가 존재하지 않는 경우")
    @Test
    void findById_NotExistsId_ReturnEmpty() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);
        Product savedProduct = productDao.save(product);

        Optional<Product> foundProduct = productDao.findById(savedProduct.getId() + 1);

        assertThat(foundProduct.isPresent()).isFalse();
    }

    @DisplayName("전체 Product 조회 - 성공")
    @Test
    void findAll_Success() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);
        Product savedProduct = productDao.save(product);

        List<Product> products = productDao.findAll();

        assertThat(products).isNotNull();
        assertThat(products).isNotEmpty();

        List<Long> productIds = products.stream()
            .map(Product::getId)
            .collect(Collectors.toList());

        assertThat(productIds).contains(savedProduct.getId());
    }
}
