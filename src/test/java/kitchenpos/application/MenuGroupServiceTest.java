package kitchenpos.application;

import static org.mockito.Mockito.verify;

import kitchenpos.Fixtures;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroup menuGroup = Fixtures.makeMenuGroup();

        menuGroupService.create(menuGroup);

        verify(menuGroupDao).save(menuGroup);
    }

    @DisplayName("메뉴 그룹 불러오기")
    @Test
    void list() {
        menuGroupService.list();

        verify(menuGroupDao).findAll();

    }
}
