package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 생성")
    void create() {
        MenuGroup menuGroup = createMenuGroup(1L, "메뉴");
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup);

        MenuGroup result = menuGroupService.create(menuGroup);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회")
    void list() {
        MenuGroup menuGroup = createMenuGroup(1L, "메뉴");
        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(menuGroup));

        List<MenuGroup> result = menuGroupService.list();

        assertThat(result).isNotNull();
        assertThat(result).containsExactly(menuGroup);
    }
}
