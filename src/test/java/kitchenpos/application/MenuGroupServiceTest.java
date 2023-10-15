package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("menuGroup");
        final MenuGroup menuGroup = new MenuGroup("menuGroup");
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        // when
        final MenuGroup result = menuGroupService.create(request);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        final List<MenuGroup> menuGroups = List.of(new MenuGroup("group1"), new MenuGroup("group2"));
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(menuGroups);
    }
}
