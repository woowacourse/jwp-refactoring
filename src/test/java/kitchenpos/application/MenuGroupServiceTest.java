package kitchenpos.application;

import static org.mockito.Mockito.verify;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_생성할_수_있다() {
        MenuGroup 메뉴그룹 = MenuGroupFixtures.메뉴그룹();
        menuGroupService.create(메뉴그룹);
        verify(menuGroupDao).save(메뉴그룹);
    }

    @Test
    void 전체_메뉴_그룹_조회할_수_있다() {
        menuGroupService.list();
        verify(menuGroupDao).findAll();
    }
}
