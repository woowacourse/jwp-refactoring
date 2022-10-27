package kitchenpos.dao;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.MenuGroupFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class MenuDaoTest {

    @Autowired
    DataSource dataSource;

    MenuDao sut;
    MenuGroupDao menuGroupDao;


    @BeforeEach
    void setUp() {
        sut = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("Menu를 저장하고 저장된 Menu를 반환한다")
    void save() {
        // given
        Menu menu = aMenu(savedMenuGroup().getId())
                .build();

        // when
        Menu savedMenu = sut.save(menu);

        // then
        Menu findMenu = sut.findById(savedMenu.getId()).get();
        assertThat(savedMenu).isEqualTo(findMenu);
    }

    @Test
    @DisplayName("입력받은 id에 해당하는 Menu가 없으면 빈 객체를 반환한다")
    void returnOptionalEmpty_WhenFindByNonExistId() {
        Optional<Menu> findMenu = sut.findById(0L);
        assertThat(findMenu).isEmpty();
    }

    @Test
    @DisplayName("저장된 모든 Menu를 조회한다")
    void findAll() {
        // given
        List<Menu> previousSaved = sut.findAll();
        sut.save(
                aMenu(savedMenuGroup().getId())
                        .build()
        );

        // when
        List<Menu> actual = sut.findAll();

        // then
        assertThat(actual.size()).isEqualTo(previousSaved.size() + 1);
    }

    @Test
    @DisplayName("입력받은 id 리스트에 해당하는 Menu의 개수를 반환한다")
    void countByIdIn() {
        // given
        Long menuId1 = sut.save(
                aMenu(savedMenuGroup().getId())
                        .build()
        ).getId();

        Long menuId2 = sut.save(
                aMenu(savedMenuGroup().getId())
                        .build()
        ).getId();

        // when
        long count = sut.countByIdIn(List.of(menuId1, menuId2));

        // then
        assertThat(count).isEqualTo(2);
    }

    private MenuGroup savedMenuGroup() {
        return menuGroupDao.save(createMenuGroup());
    }
}
