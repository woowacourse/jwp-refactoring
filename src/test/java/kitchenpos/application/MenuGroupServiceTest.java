package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create_menuGroup() {
        // given
        final MenuGroup newMenuGroup = new MenuGroup();
        newMenuGroup.setId(1L);
        newMenuGroup.setName("메뉴 그룹");

        given(menuGroupDao.save(newMenuGroup))
                .willReturn(newMenuGroup);

        // when
        final MenuGroup result = menuGroupService.create(newMenuGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(newMenuGroup.getId());
            softly.assertThat(result.getName()).isEqualTo(newMenuGroup.getName());
        });
    }

    @DisplayName("전체 메뉴 그룹을 가져온다.")
    @Test
    void find_all_menuGroup() {
        // given
        final MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setId(1L);
        menuGroup1.setName("메뉴 그룹1");

        final MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setId(2L);
        menuGroup2.setName("메뉴 그룹2");

        final List<MenuGroup> menuGroups = List.of(menuGroup1, menuGroup2);

        given(menuGroupDao.findAll())
                .willReturn(menuGroups);

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(menuGroups);
    }
}
