package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;
    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        //given
        final MenuGroup menuGroup = MenuGroup.from("추천메뉴");
        final MenuGroup result = new MenuGroup(1L, "추천메뉴");
        given(menuGroupDao.save(menuGroup)).willReturn(result);

        //when
        menuGroupService.create(menuGroup);

        //then
        assertThat(menuGroup).usingRecursiveComparison().ignoringFields("id").isEqualTo(result);
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다.")
    void list() {
        //given
        final List<MenuGroup> menuGroups = List.of(
                new MenuGroup(1L, "추천"),
                new MenuGroup(2L, "추천메"),
                new MenuGroup(3L, "추천메뉴"));
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        //when
        final List<MenuGroup> result = menuGroupService.list();


        then(menuGroupDao).should(times(1)).findAll();
        assertThat(result).hasSize(3);
    }
}
