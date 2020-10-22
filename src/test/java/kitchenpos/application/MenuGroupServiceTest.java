package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

        MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual.getId()).isEqualTo(menuGroup.getId());
    }

    @Test
    void findAll() {
        List<MenuGroup> menuGroups = Arrays.asList(new MenuGroup(), new MenuGroup(), new MenuGroup());
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(menuGroups.size());
    }
}