package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @MockBean
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenu() {
        MenuGroup menuGroup = createMenuGroup(1L, "음료");

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup);
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertAll(
            () -> assertThat(savedMenuGroup.getId()).isNotNull(),
            () -> assertThat(savedMenuGroup.getName()).isEqualTo("음료")
        );
    }

    @DisplayName("메뉴 리스트를 조회한다.")
    @Test
    void list() {
        MenuGroup menuGroup = createMenuGroup(1L, "음료");

        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
            () -> assertThat(menuGroups).hasSize(1),
            () -> assertThat(menuGroups.get(0).getId()).isNotNull(),
            () -> assertThat(menuGroups.get(0).getName()).isEqualTo("음료")
        );
    }
}
