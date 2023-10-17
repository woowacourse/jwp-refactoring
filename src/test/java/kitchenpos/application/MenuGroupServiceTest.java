package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu.MenuGroup;
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
        MenuGroup expectedFirstMenuGroup = new MenuGroup();
        expectedFirstMenuGroup.setId(1L);
        expectedFirstMenuGroup.setName("고기");

        MenuGroup expectedSecondMenuGroup = new MenuGroup();
        expectedSecondMenuGroup.setId(2L);
        expectedSecondMenuGroup.setName("물고기");

        List<MenuGroup> expected = List.of(expectedFirstMenuGroup, expectedSecondMenuGroup);

        MenuGroup mockFirstMenuGroup = new MenuGroup();
        mockFirstMenuGroup.setId(1L);
        mockFirstMenuGroup.setName("고기");

        MenuGroup mockSecondMenuGroup = new MenuGroup();
        mockSecondMenuGroup.setId(2L);
        mockSecondMenuGroup.setName("물고기");

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
        MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName("고기");

        MenuGroup argumentMenuGroup = new MenuGroup();
        argumentMenuGroup.setName("고기");

        MenuGroup mockReturnMenuGroup = new MenuGroup();
        mockReturnMenuGroup.setId(1L);
        mockReturnMenuGroup.setName("고기");

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
        MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName("");

        MenuGroup argumentMenuGroup = new MenuGroup();
        argumentMenuGroup.setName("");

        MenuGroup mockReturnMenuGroup = new MenuGroup();
        mockReturnMenuGroup.setId(1L);
        mockReturnMenuGroup.setName("");

        BDDMockito.given(menuGroupDao.save(argumentMenuGroup))
                .willReturn(mockReturnMenuGroup);

        // when
        MenuGroup actual = menuGroupService.create(argumentMenuGroup);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
