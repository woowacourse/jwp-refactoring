package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuDaoTest {

    @Autowired
    DataSource dataSource;

    MenuDao menuDao;

    @BeforeEach
    void setUp() {
        menuDao = new JdbcTemplateMenuDao(dataSource);
    }

    @Test
    @DisplayName("Menu를 저장하고 저장된 Menu를 반환한다")
    void save() {
        // given
        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setPrice(BigDecimal.valueOf(1000L));
        menu.setMenuGroupId(1L);

        // when
        Menu savedMenu = menuDao.save(menu);

        // then
        Menu findMenu = menuDao.findById(savedMenu.getId()).get();
        assertThat(savedMenu).isEqualTo(findMenu);
    }

    @Test
    @DisplayName("입력받은 id에 해당하는 Menu가 없으면 빈 객체를 반환한다")
    void returnOptionalEmpty_WhenFindByNonExistId() {
        Optional<Menu> findMenu = menuDao.findById(0L);
        assertThat(findMenu).isEmpty();
    }

    @Test
    @DisplayName("저장된 모든 Menu를 조회한다")
    void findAll() {
        // given
        List<Menu> previousSaved = menuDao.findAll();
        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setPrice(BigDecimal.valueOf(1000L));
        menu.setMenuGroupId(1L);
        menuDao.save(menu);

        // when
        List<Menu> actual = menuDao.findAll();

        // then
        assertThat(actual.size()).isEqualTo(previousSaved.size() + 1);
    }

    @Test
    @DisplayName("입력받은 id 리스트에 해당하는 Menu의 개수를 반환한다")
    void countByIdIn() {
        // given
        Menu menu1 = new Menu();
        menu1.setName("강정치킨");
        menu1.setPrice(BigDecimal.valueOf(1000L));
        menu1.setMenuGroupId(1L);
        Long menuId1 = menuDao.save(menu1).getId();

        Menu menu2 = new Menu();
        menu2.setName("순살치킨");
        menu2.setPrice(BigDecimal.valueOf(1000L));
        menu2.setMenuGroupId(1L);
        Long menuId2 = menuDao.save(menu2).getId();

        // when
        long count = menuDao.countByIdIn(List.of(menuId1, menuId2));

        // then
        assertThat(count).isEqualTo(2);
    }
}
