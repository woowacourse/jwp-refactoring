package kitchenpos.application;

import kitchenpos.application.dto.CreateMenuGroupDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupName;
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
        MenuGroupDto expectedFirstMenuGroup = new MenuGroupDto(1L, "고기");
        MenuGroupDto expectedSecondMenuGroup = new MenuGroupDto(2L, "물고기");
        List<MenuGroupDto> expected = List.of(expectedFirstMenuGroup, expectedSecondMenuGroup);

        MenuGroup mockFirstMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName("고기"));
        MenuGroup mockSecondMenuGroup = new MenuGroup(
                2L,
                new MenuGroupName("물고기"));

        BDDMockito.given(menuGroupDao.findAll())
                .willReturn(List.of(mockFirstMenuGroup, mockSecondMenuGroup));

        // when
        List<MenuGroupDto> actual = menuGroupService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 메뉴그룹을_추가한다() {
        // given
        MenuGroupDto expected = new MenuGroupDto(1L, "고기");

        CreateMenuGroupDto createMenuGroupDto = new CreateMenuGroupDto("고기");

        MenuGroup mockReturnMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName("고기"));

        BDDMockito.given(menuGroupDao.save(BDDMockito.any(MenuGroup.class)))
                .willReturn(mockReturnMenuGroup);

        // when
        MenuGroupDto actual = menuGroupService.create(createMenuGroupDto);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void 메뉴그룹을_추가할_때_이름이_공백일_수_있다() {
        // given
        MenuGroupDto expected = new MenuGroupDto(1L, "");

        CreateMenuGroupDto createMenuGroupDto = new CreateMenuGroupDto("");

        MenuGroup mockReturnMenuGroup = new MenuGroup(
                1L,
                new MenuGroupName(""));

        BDDMockito.given(menuGroupDao.save(BDDMockito.any(MenuGroup.class)))
                .willReturn(mockReturnMenuGroup);

        // when
        MenuGroupDto actual = menuGroupService.create(createMenuGroupDto);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
