package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuGroupServiceTest {

    private MenuGroupService sut;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        sut = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup("두마리메뉴");

        // when
        final MenuGroup createdMenuGroup = sut.create(menuGroup);

        // then
        assertThat(createdMenuGroup).isNotNull();
        assertThat(createdMenuGroup.getId()).isNotNull();
        final MenuGroup foundMenuGroup = menuGroupDao.findById(createdMenuGroup.getId()).get();
        assertThat(foundMenuGroup)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<MenuGroup> menuGroups = sut.list();

        // then
        assertThat(menuGroups)
                .hasSize(4)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"
                );
    }
}
