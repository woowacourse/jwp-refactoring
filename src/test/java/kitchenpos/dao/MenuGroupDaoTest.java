package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class MenuGroupDaoTest {
    @Autowired
    private DataSource dataSource;
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Test
    @DisplayName("메뉴그룹을 저장한다.")
    public void save() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Group1");

        //when
        MenuGroup returnedMenuGroup = menuGroupDao.save(menuGroup);

        //then
        assertThat(returnedMenuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo(returnedMenuGroup.getName());
    }

    @Test
    @DisplayName("메뉴그룹을 조회한다.")
    public void findById() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Group1");
        Long id = menuGroupDao.save(menuGroup).getId();

        //when
        Optional<MenuGroup> returnedMenuGroup = menuGroupDao.findById(id);

        //then
        assertThat(returnedMenuGroup.isPresent()).isTrue();
        assertThat(returnedMenuGroup.get().getId()).isEqualTo(id);
        assertThat(returnedMenuGroup.get().getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("모든 메뉴그룹을 조회한다.")
    public void findAll() {
        //given
        final int originalSize = menuGroupDao.findAll().size();

        MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setId(1L);
        menuGroup1.setName("Group1");
        menuGroupDao.save(menuGroup1);

        MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setId(2L);
        menuGroup2.setName("Group2");
        menuGroupDao.save(menuGroup2);

        //when
        List<MenuGroup> returnedMenuGroups = menuGroupDao.findAll();

        //then
        assertThat(returnedMenuGroups).hasSize(2 + originalSize);
        assertThat(returnedMenuGroups).contains(menuGroup1);
        assertThat(returnedMenuGroups).contains(menuGroup2);
    }

    @Test
    @DisplayName("메뉴그룹이 존재하는지 확인한다.")
    public void existsById() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Group1");
        Long id = menuGroupDao.save(menuGroup).getId();

        //when
        boolean exists = menuGroupDao.existsById(id);

        //then
        assertThat(exists).isTrue();
    }
}
