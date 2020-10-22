package kitchenpos.application;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.factory.MenuGroupFactory;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupFactory menuGroupFactory = new MenuGroupFactory();

    @Test
    void create() {
        MenuGroup menuGroup = menuGroupFactory.create("추천 메뉴");

        menuGroupService.create(menuGroup);

        verify(menuGroupDao).save(menuGroup);
    }

    @Test
    void list() {
        menuGroupService.list();

        verify(menuGroupDao).findAll();
    }
}