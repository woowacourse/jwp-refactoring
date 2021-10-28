package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = menuGroupConstructor("치킨류");
        MenuGroup expected = menuGroupConstructor(1L, "치킨류");
        given(menuGroupDao.save(menuGroup)).willReturn(expected);

        //when
        when(menuGroupDao.save(menuGroup)).thenAnswer(invocation -> {
            MenuGroup argument = invocation.getArgument(0);
            argument.setId(1L);
            return argument;
        });
        MenuGroup actual = menuGroupDao.save(menuGroup);

        //then
        assertThat(menuGroup).usingRecursiveComparison().isEqualTo(actual);
    }

    @DisplayName("메뉴 그룹을 읽는다.")
    @Test
    void readAll() {
        //given
        List<MenuGroup> expected = Arrays.asList(
            menuGroupConstructor(1L, "치킨류"),
            menuGroupConstructor(2L, "사이드 메뉴"),
            menuGroupConstructor(3L, "음료류"),
            menuGroupConstructor(4L, "주류")
        );
        given(menuGroupService.list()).willReturn(expected);

        //when
        List<MenuGroup> actual = menuGroupService.list();

        //then
        assertEquals(actual, expected);
    }

    private MenuGroup menuGroupConstructor(final String name) {
        return menuGroupConstructor(null, name);
    }

    private MenuGroup menuGroupConstructor(final Long id, final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}
