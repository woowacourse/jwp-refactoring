package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {


    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void testCreateSuccess() {
        //given
        final MenuGroup expected = new MenuGroup(1L, "test");
        when(menuGroupDao.save(any()))
                .thenReturn(expected);

        //when
        final MenuGroup result = menuGroupService.create(any());

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testListSuccess() {
        //given
        final List<MenuGroup> expected = List.of(new MenuGroup(1L, "test"));
        when(menuGroupDao.findAll())
                .thenReturn(expected);

        //when
        final List<MenuGroup> result = menuGroupService.list();

        //then
        assertThat(result).isEqualTo(expected);
    }
}
