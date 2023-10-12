package kitchenpos.application;

import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create_success() {
        MenuGroup menuGroup = new MenuGroup();
        MenuGroup savedMenuGroup = new MenuGroup();
        when(menuGroupDao.save(menuGroup)).thenReturn(savedMenuGroup);

        Assertions.assertThat(menuGroupService.create(menuGroup)).isEqualTo(savedMenuGroup);
    }

    @Test
    @DisplayName("현재 저장된 메뉴 그룹 목록을 확인할 수 있다.")
    void list_success() {
        MenuGroup menuGroup1 = new MenuGroup();
        MenuGroup menuGroup2 = new MenuGroup();
        when(menuGroupDao.findAll()).thenReturn(List.of(menuGroup1, menuGroup2));

        Assertions.assertThat(menuGroupService.list()).containsExactlyInAnyOrder(menuGroup1, menuGroup2);
    }
}
