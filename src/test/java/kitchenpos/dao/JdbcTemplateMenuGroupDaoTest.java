package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static kitchenpos.testutils.TestDomainBuilder.menuGroupBuilder;
import static org.assertj.core.api.Assertions.assertThat;

@Import(JdbcTemplateMenuGroupDao.class)
class JdbcTemplateMenuGroupDaoTest extends AbstractJdbcTemplateDaoTest {

    private static final Long NON_EXISTENT_ID = 987654321L;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup doubleQuantityMenuGroup, singleQuantityMenuGroup;

    @BeforeEach
    void setUp() {
        doubleQuantityMenuGroup = menuGroupDao.save(
                menuGroupBuilder().name("두마리메뉴").build()
        );
        singleQuantityMenuGroup = menuGroupDao.save(
                menuGroupBuilder().name("한마리메뉴").build()
        );
    }

    @DisplayName("메뉴 그룹을 저장한다.")
    @Test
    void save() {
        // given
        String name = "순살파닭두마리메뉴";
        MenuGroup newMenuGroup = menuGroupBuilder().name(name).build();

        // when
        MenuGroup menuGroup = menuGroupDao.save(newMenuGroup);

        // then
        assertThat(menuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo(name);
    }

    @DisplayName("존재하는 메뉴 그룹을 조회한다.")
    @Test
    void findByIdWhenExistent() {
        // given
        Long id = doubleQuantityMenuGroup.getId();

        // when
        Optional<MenuGroup> optionalMenuGroup = menuGroupDao.findById(id);

        // then
        assertThat(optionalMenuGroup).get()
                .usingRecursiveComparison()
                .isEqualTo(doubleQuantityMenuGroup);
    }

    @DisplayName("존재하지 않는 메뉴 그룹을 조회한다.")
    @Test
    void findByIdWhenNonexistent() {
        // when
        Optional<MenuGroup> optionalMenuGroup = menuGroupDao.findById(NON_EXISTENT_ID);

        // then
        assertThat(optionalMenuGroup).isEmpty();
    }

    @DisplayName("전체 메뉴 그룹을 가져온다.")
    @Test
    void findAll() {
        // when
        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        // then
        assertThat(menuGroups)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(doubleQuantityMenuGroup, singleQuantityMenuGroup);
    }

    @DisplayName("메뉴 그룹이 존재하는지 확인한다.")
    @Test
    void existsById() {
        // given
        Long existentId = doubleQuantityMenuGroup.getId();

        // when, then
        assertThat(menuGroupDao.existsById(existentId)).isTrue();
        assertThat(menuGroupDao.existsById(NON_EXISTENT_ID)).isFalse();
    }
}
