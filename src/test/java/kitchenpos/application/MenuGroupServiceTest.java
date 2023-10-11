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

import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다")
    void create() {
        //given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("떡볶이");

        //when
        menuGroupService.create(menuGroup);

        //then
        verify(menuGroupDao, times(1)).save(menuGroup);
    }

    @Test
    @DisplayName("메뉴 그룹 전체 조회할 수 있다")
    void list() {
        //when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        verify(menuGroupDao, times(1)).findAll();
    }
}
