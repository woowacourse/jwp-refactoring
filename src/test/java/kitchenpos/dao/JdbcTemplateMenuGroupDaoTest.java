package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.DomainFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateMenuGroupDaoTest extends JdbcTemplateDaoTest {
    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupIds = new ArrayList<>();
    }

    @DisplayName("메뉴 그룹 저장")
    @Test
    void saveTest() {
        MenuGroup menuGroup = createMenuGroup("두마리메뉴");

        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);
        menuGroupIds.add(savedMenuGroup.getId());

        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo("두마리메뉴")
        );
    }

    @DisplayName("아이디에 해당하는 메뉴 그룹이 존재하는지 확인한다.")
    @Test
    void findByIdTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

        MenuGroup findMenuGroup = jdbcTemplateMenuGroupDao.findById(savedMenuGroup.getId()).get();
        menuGroupIds.add(findMenuGroup.getId());

        assertAll(
                () -> assertThat(findMenuGroup.getId()).isEqualTo(savedMenuGroup.getId()),
                () -> assertThat(findMenuGroup.getName()).isEqualTo(savedMenuGroup.getName())
        );
    }

    @DisplayName("존재하지 않는 메뉴 그룹 아이디 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidMenuGroupIdTest() {
        Optional<MenuGroup> findMenuGroup = jdbcTemplateMenuGroupDao.findById(0L);

        assertThat(findMenuGroup).isEqualTo(Optional.empty());
    }

    @DisplayName("저장된 모든 메뉴 그룹을 조회한다.")
    @Test
    void findAllTest() {
        MenuGroup singleMenuGroup = createMenuGroup("한마리메뉴");
        MenuGroup doubleMenuGroup = createMenuGroup("두마리메뉴");
        jdbcTemplateMenuGroupDao.save(singleMenuGroup);
        jdbcTemplateMenuGroupDao.save(doubleMenuGroup);

        List<MenuGroup> allMenuGroups = jdbcTemplateMenuGroupDao.findAll();
        allMenuGroups.forEach(menuGroup -> menuGroupIds.add(menuGroup.getId()));

        assertThat(allMenuGroups).hasSize(2);
    }

    @DisplayName("아이디에 해당하는 메뉴 그룹이 존재하는지 확인")
    @Test
    void existsByIdTest() {
        MenuGroup menuGroup = createMenuGroup("한마리메뉴");
        MenuGroup savedMenuGroup = jdbcTemplateMenuGroupDao.save(menuGroup);

        boolean isExistMenuGroup = jdbcTemplateMenuGroupDao.existsById(savedMenuGroup.getId());
        assertThat(isExistMenuGroup).isTrue();
    }

    @AfterEach
    void tearDown() {
        deleteMenuGroup();
    }
}