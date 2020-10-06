package kitchenpos.dao;

import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuGroupDaoTest extends DaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    private String menuGroupName;

    @BeforeEach
    void setUp() {
        menuGroupName = "추천 메뉴";
    }

    @DisplayName("메뉴 그룹 save - 성공")
    @Test
    void save() {
        MenuGroup menuGroup = createMenuGroup(menuGroupName);
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        assertAll(() -> {
            assertThat(savedMenuGroup.getId()).isNotNull();
            assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupName);
        });
    }

    @DisplayName("메뉴 그룹 findById - 성공")
    @Test
    void findById() {
        MenuGroup menuGroup = createMenuGroup(menuGroupName);
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(savedMenuGroup.getId());

        assertThat(foundMenuGroup.isPresent()).isTrue();
    }

    @DisplayName("메뉴 그룹 findById - 예외, 빈 데이터에 접근하려고 하는 경우")
    @Test
    void findById_EmptyResultDataAccess_ThrownException() {
        Optional<MenuGroup> foundMenuGroup = menuGroupDao.findById(NOT_EXIST_VALUE);

        assertThat(foundMenuGroup.isPresent()).isFalse();
    }

    @DisplayName("메뉴 그룹 findAll - 성공")
    @Test
    void findAll() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        assertThat(menuGroups).hasSize(4);
    }

    @DisplayName("메뉴 그룹 existsById - 성공")
    @Test
    void existsById() {
        MenuGroup menuGroup = createMenuGroup(menuGroupName);
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        assertThat(menuGroupDao.existsById(savedMenuGroup.getId())).isTrue();
    }
}
