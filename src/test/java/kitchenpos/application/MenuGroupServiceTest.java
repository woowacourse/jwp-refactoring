package kitchenpos.application;

import static org.mockito.Mockito.verify;

import kitchenpos.annotation.MockTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@MockTest
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_저장한다() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Fried Chicken");

        // when
        menuGroupService.create(menuGroup);

        // then
        verify(menuGroupDao).save(menuGroup);
    }

    @Test
    void 저장된_모든_메뉴_그룹을_조회한다() {
        // when
        menuGroupService.list();

        // then
        verify(menuGroupDao).findAll();
    }
}
