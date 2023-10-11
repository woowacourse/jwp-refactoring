package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
        final MenuGroup menuGroup = new MenuGroup(1L, "menuGroup");
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        // when
        final MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(menuGroup);
    }

    @Test
    void list() {
        // given
        final List<MenuGroup> menuGroups = List.of(
                new MenuGroup(1L, "group1"),
                new MenuGroup(2L, "group2")
        );
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        // when
        final List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(menuGroups);
    }
}
