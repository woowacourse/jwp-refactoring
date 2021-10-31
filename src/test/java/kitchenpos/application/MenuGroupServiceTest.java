package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록 할 수 있다.")
    @Test
    void testCreateMenuGroup() {
        //given
        MenuGroup menuGroup = new MenuGroup();
        given(menuGroupDao.save(any())).willReturn(치킨());

        //when
        MenuGroup actual = menuGroupService.create(menuGroup);

        //then
        assertThat(actual.getId()).isEqualTo(1L);
    }

    @DisplayName("메뉴그룹 리스트를 조회 할 수 있다.")
    @Test
    void testList() {
        //given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(치킨(), 디저트(), 양식()));

        //when
        List<MenuGroup> actual = menuGroupService.list();

        //then
        verify(menuGroupDao, times(1)).findAll();
        assertThat(actual).hasSize(3);
    }
}