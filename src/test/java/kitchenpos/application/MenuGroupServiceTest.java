package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @MockBean
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void createMenu() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("음료");
        menuGroup.setId(1L);

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
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("음료");
        menuGroup.setId(1L);

        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
            () -> assertThat(menuGroups).hasSize(1),
            () -> assertThat(menuGroups.get(0).getId()).isNotNull(),
            () -> assertThat(menuGroups.get(0).getName()).isEqualTo("음료")
        );
    }
}
