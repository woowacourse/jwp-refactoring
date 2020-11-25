package kitchenpos.dao;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import(JdbcTemplateProductDao.class)
@Sql("/data-for-dao.sql")
class JdbcTemplateProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @DisplayName("Product entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 seq를 가져온다.")
    @Test
    void save() {
        Product 새제품 = productDao.save(createProduct("간장치킨", BigDecimal.valueOf(15_000)));
        assertThat(새제품.getId()).isNotNull();
    }

    @DisplayName("존재하는 Product seq로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<Product> id를_통해_조회한_제품 = productDao.findById(1L);
        assertThat(id를_통해_조회한_제품).isNotEmpty();
    }

    @DisplayName("존재하지 않는 Product seq로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<Product> 데이터베이스에_존재하지않는_제품 = productDao.findById(9999L);
        assertThat(데이터베이스에_존재하지않는_제품).isEmpty();
    }

    @DisplayName("database에 존재하는 Product 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<Product> actual = productDao.findAll();
        assertThat(actual).hasSize(6);
    }
}