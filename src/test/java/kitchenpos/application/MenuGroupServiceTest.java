package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.generator.MenuGroupGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuGroupServiceTest extends ServiceTest {

    @Mock
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        when(menuGroupDao.save(any(MenuGroup.class))).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        MenuGroup menuGroup = MenuGroupGenerator.newInstance("추천메뉴");
        MenuGroup actual = menuGroupService.create(menuGroup);

        verify(menuGroupDao, times(1)).save(menuGroup);
        assertThat(actual).isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        List<MenuGroup> expected = Arrays.asList(new MenuGroup(), new MenuGroup(), new MenuGroup());
        when(menuGroupDao.findAll()).thenReturn(expected);

        List<MenuGroup> actual = menuGroupService.list();

        verify(menuGroupDao, times(1)).findAll();
        assertThat(actual).hasSameSizeAs(expected)
            .containsExactlyElementsOf(expected);
    }
}
