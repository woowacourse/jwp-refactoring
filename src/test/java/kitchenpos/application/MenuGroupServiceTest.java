package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixtures.MenuGroupFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class MenuGroupServiceTest extends ServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixtures.createMenuGroup();
    }

    @Test
    void 메뉴_그룹을_생성한다() {
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        assertDoesNotThrow(() -> menuGroupService.create(MenuGroupFixtures.createMenuGroupRequest()));
    }

    @Test
    void 메뉴_그룹_리스트를_반환한다() {
        MenuGroup menuGroup2 = MenuGroupFixtures.createMenuGroup();
        List<MenuGroup> menuGroups = new ArrayList<>();
        menuGroups.add(menuGroup);
        menuGroups.add(menuGroup2);
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        assertDoesNotThrow(() -> menuGroupService.list());
    }
}