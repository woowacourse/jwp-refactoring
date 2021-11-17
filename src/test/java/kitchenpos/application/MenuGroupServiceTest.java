package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup.MenuGroupBuilder()
                                .setName("치킨")
                                .build();
        MenuGroup expected = new MenuGroup.MenuGroupBuilder()
                                .setId(1L)
                                .setName("치킨")
                                .build();

        given(menuGroupDao.save(menuGroup))
                .willReturn(expected);
        //when
        MenuGroup actual = menuGroupService.create(menuGroup);
        //then
        assertThat(actual).isEqualTo(expected);

        verify(menuGroupDao, times(1)).save(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록을 불러온다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroupChicken = new MenuGroup.MenuGroupBuilder()
                                        .setId(1L)
                                        .setName("치킨")
                                        .build();

        MenuGroup menuGroupPizza = new MenuGroup.MenuGroupBuilder()
                                        .setId(2L)
                                        .setName("피자")
                                        .build();

        List<MenuGroup> expected = List.of(menuGroupChicken, menuGroupPizza);

        given(menuGroupDao.findAll())
                .willReturn(expected);
        //when
        List<MenuGroup> actual = menuGroupService.list();
        //then

        assertThat(actual).isEqualTo(expected);

        verify(menuGroupDao, times(1)).findAll();
    }
}