package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("test_group");

        MenuGroup savedMenuGroup = new MenuGroup();
        savedMenuGroup.setId(1L);
        savedMenuGroup.setName(menuGroup.getName());

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(savedMenuGroup);

        MenuGroup expected = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(expected).extracting(MenuGroup::getId).isEqualTo(savedMenuGroup.getId()),
            () -> assertThat(expected).extracting(MenuGroup::getName).isEqualTo(savedMenuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("Test");

        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(menuGroup));
        List<MenuGroup> expected = menuGroupService.list();

        assertThat(expected).hasSize(1);
    }
}