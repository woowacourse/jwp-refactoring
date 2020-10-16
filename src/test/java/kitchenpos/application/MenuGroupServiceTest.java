package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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
    private MenuGroupDao dao;

    @InjectMocks
    private MenuGroupService service;

    private MenuGroup menuGroup;

    @BeforeEach
    public void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("패스트 푸드");
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    public void createMenuGroup() {
        given(dao.save(any(MenuGroup.class))).willReturn(menuGroup);

        final MenuGroup savedMenuGroup = service.create(this.menuGroup);

        assertThat(savedMenuGroup.getId()).isEqualTo(1L);
        assertThat(savedMenuGroup.getName()).isEqualTo("패스트 푸드");
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    public void readMenuGroup() {
        given(dao.findAll()).willReturn(Lists.newArrayList(menuGroup));

        final List<MenuGroup> menuGroups = service.list();

        assertThat(menuGroups).hasSize(1);
        assertThat(menuGroups.get(0).getName()).isEqualTo("패스트 푸드");
    }
}