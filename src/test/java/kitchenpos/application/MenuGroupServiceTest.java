package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_생성() {
        // given
        MenuGroup menuGroup = new MenuGroup(null, "menuGroup");
        MenuGroup savedMenuGroup = new MenuGroup(1L, "menuGroup");
        given(menuGroupDao.save(any()))
            .willReturn(savedMenuGroup);

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertSoftly(softly -> {
            assertThat(actual).isEqualTo(savedMenuGroup);
            verify(menuGroupDao, times(1)).save(any());
        });
    }

    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = List.of(new MenuGroup(1L, "menuGroup1"),
            new MenuGroup(2L, "menuGroup2"));
        given(menuGroupDao.findAll())
            .willReturn(menuGroups);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertSoftly(softly -> {
            assertThat(actual).isEqualTo(menuGroups);
            verify(menuGroupDao, times(1)).findAll();
        });
    }

}
