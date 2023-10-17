package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu.MenuGroup;
import kitchenpos.domain.Menu.MenuGroupName;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

class MenuGroupServiceTest extends MockServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴그룹_목록을_조회한다() {
        // given
        MenuGroup expectedFirstMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName("고기"));
        MenuGroup expectedSecondMenuGroup = new MenuGroup(
                2L,
                new MenuGroupName("물고기"));
        List<MenuGroup> expected = List.of(expectedFirstMenuGroup, expectedSecondMenuGroup);

        MenuGroup mockFirstMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName("고기"));
        MenuGroup mockSecondMenuGroup = new MenuGroup(
                2L,
                new MenuGroupName("물고기"));

        BDDMockito.given(menuGroupDao.findAll())
                .willReturn(List.of(mockFirstMenuGroup, mockSecondMenuGroup));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 메뉴그룹을_추가한다() {
        // given
        MenuGroup expected = new MenuGroup(
                1L,
                new MenuGroupName("고기"));

        MenuGroup argumentMenuGroup = new MenuGroup(
                new MenuGroupName("고기"));

        MenuGroup mockReturnMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName("고기"));

        BDDMockito.given(menuGroupDao.save(argumentMenuGroup))
                .willReturn(mockReturnMenuGroup);

        // when
        MenuGroup actual = menuGroupService.create(argumentMenuGroup);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 메뉴그룹을_추가할_때_이름이_공백일_수_있다() {
        // given
        MenuGroup expected = new MenuGroup(
                1L,
                new MenuGroupName(""));

        MenuGroup argumentMenuGroup = new MenuGroup(
                new MenuGroupName(""));

        MenuGroup mockReturnMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName(""));

        BDDMockito.given(menuGroupDao.save(argumentMenuGroup))
                .willReturn(mockReturnMenuGroup);

        // when
        MenuGroup actual = menuGroupService.create(argumentMenuGroup);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
