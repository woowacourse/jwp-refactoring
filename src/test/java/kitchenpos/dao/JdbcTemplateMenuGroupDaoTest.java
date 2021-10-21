package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("메뉴그룹Dao 테스트")
class JdbcTemplateMenuGroupDaoTest extends DaoTest {

    private JdbcTemplateMenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }

    @DisplayName("메뉴그룹을 저장한다.")
    @Test
    void save() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");

        // when - then
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("id로 메뉴그룹을 조회한다.")
    @Test
    void findById() {
        // given
        long menuGroupId = SAVE_MENU_GROUP();

        // when
        Optional<MenuGroup> findMenuGroup = menuGroupDao.findById(menuGroupId);

        // then
        assertThat(findMenuGroup).isPresent();
        MenuGroup menuGroup = findMenuGroup.get();
        assertThat(menuGroup.getId()).isEqualTo(menuGroupId);
    }

    @DisplayName("등록된 모든 메뉴 그룹을 조회한다.")
    @Test
    void findAll() {
        // given
        SAVE_MENU_GROUP();

        // when
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        // 초기화를 통해 등록된 메뉴 4개 + 새로 추가한 메뉴 1개
        assertThat(menuGroups).hasSize(4 + 1);
    }

    @DisplayName("id에 해당하는 메뉴 그룹이 존재하는지 확인한다.")
    @Test
    void existsById() {
        // given
        long menuGroupId = SAVE_MENU_GROUP();

        // when
        boolean flag = menuGroupDao.existsById(menuGroupId);

        // then
        assertTrue(flag);
    }

    @DisplayName("id에 해당하는 메뉴 그룹이 존재하는지 확인한다.")
    @Test
    void notExistsById() {
        // given
        long menuGroupId = -1L;

        // when
        boolean flag = menuGroupDao.existsById(menuGroupId);

        // then
        assertFalse(flag);
    }

    private long SAVE_MENU_GROUP() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");

        // when - then
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        return savedMenuGroup.getId();
    }
}
