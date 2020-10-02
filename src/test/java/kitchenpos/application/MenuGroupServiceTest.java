package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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

    @DisplayName("create: 메뉴 그룹 생성 테스트")
    @Test
    void createTest() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("한마리 메뉴");

        when(menuGroupDao.save(any())).thenReturn(menuGroup);

        final MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("findMenuGroups: 전체 메뉴 그룹 목록 조회 테스트")
    @Test
    void findMenuGroupsTest() {
        final MenuGroup oneMenuGroup = new MenuGroup();
        oneMenuGroup.setName("한마리 메뉴");
        final MenuGroup twoMenuGroup = new MenuGroup();
        twoMenuGroup.setName("두마리 메뉴");

        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(oneMenuGroup, twoMenuGroup));
        final List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(2);
    }
}

