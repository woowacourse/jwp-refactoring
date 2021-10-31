package kitchenpos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MenuGroupServiceTest extends ServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        when(menuGroupDao.save(any())).thenReturn(MenuGroupFixture.menuGroup());

        menuGroupService.create(MenuGroupFixture.menuGroup());
    }

    @DisplayName("메뉴 조회")
    @Test
    void findall() {
        when(menuGroupDao.findAll())
                .thenReturn(Arrays.asList(MenuGroupFixture.menuGroup(), MenuGroupFixture.menuGroup()));

        menuGroupService.list();
    }
}
