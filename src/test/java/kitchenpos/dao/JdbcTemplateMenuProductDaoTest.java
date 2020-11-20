package kitchenpos.dao;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import(JdbcTemplateMenuProductDao.class)
@Sql("/data-for-dao.sql")
class JdbcTemplateMenuProductDaoTest {

    @Autowired
    MenuProductDao menuProductDao;

    @DisplayName("menuproduct entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 id를 가져온다.")
    @Test
    void save() {
        MenuProduct 신규메뉴상품 = menuProductDao.save(createMenuProduct(1L, 2L, 2L));
        assertThat(신규메뉴상품.getSeq()).isNotNull();
    }

    @DisplayName("존재하는 menuproduct Id로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<MenuProduct> 존재하는메뉴상품 = menuProductDao.findById(1L);
        assertThat(존재하는메뉴상품).isNotEmpty();
    }

    @DisplayName("존재하지 않는 menuproduct Id로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<MenuProduct> 존재하지않는메뉴상품 = menuProductDao.findById(9999L);
        assertThat(존재하지않는메뉴상품).isEmpty();
    }

    @DisplayName("database에 존재하는 menuproduct 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<MenuProduct> actual = menuProductDao.findAll();
        assertThat(actual).hasSize(6);
    }

    @DisplayName("database에 존재하는 특정 메뉴에 속해있는 menuproduct 테이블 레코드 목록을 반환한다.")
    @Test
    void findAllByMenuId() {
        List<MenuProduct> actual = menuProductDao.findAllByMenuId(1L);
        assertThat(actual).hasSize(1);
    }
}