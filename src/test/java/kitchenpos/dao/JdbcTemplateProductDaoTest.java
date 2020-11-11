package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @DisplayName("Product의 id를 이용해서 IN 절을 이용해서 Product를 조회한다.")
    @Test
    void findAllByIdIn() {
        //given
        Product product1 = productDao.save(new Product("1", 1L));
        Product product2 = productDao.save(new Product("2", 1L));
        Product product3 = productDao.save(new Product("3", 1L));

        //when
        List<Product> findProducts = productDao.findAllByIdIn(Arrays.asList(product1.getId(), product2.getId()));

        //then
        assertThat(findProducts).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        productDao.deleteAll();
    }
}