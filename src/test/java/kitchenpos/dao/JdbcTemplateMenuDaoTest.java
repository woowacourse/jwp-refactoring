package kitchenpos.dao;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.Menu;

@SuppressWarnings("NonAsciiCharacters")

@Import(JdbcTemplateMenuDao.class)
class JdbcTemplateMenuDaoTest extends JdbcDaoTest {

    @Autowired
    private MenuDao menuDao;

    @DisplayName("인자로 받는 ID 목록중 DB에 존재하는 Menu Id의 갯수를 반환한다.")
    @Test
    void countByIdIn() {
        long actual = menuDao.countByIdIn(Arrays.asList(1L, 3L, 5L, 7L));
        assertThat(actual).isEqualTo(3L);
    }

    @DisplayName("Menu entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 id를 가져온다.")
    @Test
    void save() {
        Menu 닭다리세트 = menuDao.save(createMenu("닭다리세트", BigDecimal.valueOf(17000), 2L));
        assertThat(닭다리세트.getId()).isNotNull();
    }

    @DisplayName("존재하는 Menu Id로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<Menu> 존재하는메뉴 = menuDao.findById(1L);
        assertThat(존재하는메뉴).isNotEmpty();
    }

    @DisplayName("존재하지 않는 Menu Id로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<Menu> 존재하지않는메뉴 = menuDao.findById(9999L);
        assertThat(존재하지않는메뉴).isEmpty();
    }

    @DisplayName("전체 메뉴 목록 조회시, database가 가지고 있는 모든 menu 테이블의 레코드를 가져온다.")
    @Test
    void findAll() {
        List<Menu> 모든메뉴목록 = menuDao.findAll();
        assertThat(모든메뉴목록).hasSize(6);
    }
}