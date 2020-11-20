package kitchenpos.dao;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import(JdbcTemplateMenuGroupDao.class)
@Sql("/data-for-dao.sql")
class JdbcTemplateMenuGroupDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("Menu entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 id를 가져온다.")
    @Test
    void save() {
        MenuGroup 없어진메뉴 = menuGroupDao.save(createMenuGroup("없어진메뉴"));
        assertThat(없어진메뉴.getId()).isNotNull();
    }

    @DisplayName("존재하는 MenuGroup Id로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<MenuGroup> 존재하는메뉴그룹 = menuGroupDao.findById(1L);
        assertThat(존재하는메뉴그룹).isNotEmpty();
    }

    @DisplayName("존재하지 않는 MenuGroup Id로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<MenuGroup> 존재하지않는메뉴그룹 = menuGroupDao.findById(9999L);
        assertThat(존재하지않는메뉴그룹).isEmpty();
    }

    @DisplayName("database에 존재하는 menuGroup 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<MenuGroup> actual = menuGroupDao.findAll();
        assertThat(actual).hasSize(4);
    }

    @DisplayName("인자로 받는 menuGroupId값이 database에서 MenuGroup 테이블에 존재하는 레코드여부에 따라 true/false를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "999,false"})
    void existsById(Long menuGroupId, boolean expected) {
        boolean actual = menuGroupDao.existsById(menuGroupId);
        assertThat(actual).isEqualTo(expected);
    }
}