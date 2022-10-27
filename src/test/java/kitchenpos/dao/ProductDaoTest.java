package kitchenpos.dao;

import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class ProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao sut;

    @BeforeEach
    void setUp() {
        sut = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("Product를 저장하고 저정된 Product를 반환한다")
    void save() {
        Product savedProduct = sut.save(aProduct().build());

        Product findProduct = sut.findById(savedProduct.getId()).get();
        assertThat(savedProduct).usingRecursiveComparison().isEqualTo(findProduct);
    }

    @Test
    @DisplayName("존재하지 않는 product id를 입력받는 경우 빈 객체를 반환한다")
    void returnOptionalEmptyWhenGivenNonExistId() {
        final long NON_EXIST_ID = 0;
        Optional<Product> actual = sut.findById(NON_EXIST_ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("저장된 모든 Product를 반환한다")
    void findAll() {
        // given
        List<Product> previous = sut.findAll();
        sut.save(aProduct().build());

        // when
        List<Product> actual = sut.findAll();

        // then
        assertThat(actual).hasSize(previous.size() + 1);
    }
}
