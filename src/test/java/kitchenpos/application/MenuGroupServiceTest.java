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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴_그룹을_생성() {
        //given
        final MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName("name");

        given(menuGroupDao.save(expected))
                .willReturn(expected);

        //when
        final MenuGroup actual = menuGroupService.create(expected);

        //then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @DisplayName("메뉴 그룹을 전체 조회한다.")
    @Test
    void 메뉴_그룹_전체_조회() {
        //given
        final MenuGroup expected1 = new MenuGroup();
        expected1.setId(1L);
        expected1.setName("name1");

        final MenuGroup expected2 = new MenuGroup();
        expected2.setId(2L);
        expected2.setName("name2");

        final List<MenuGroup> expectedMenuGroups = List.of(expected1, expected2);

        given(menuGroupDao.findAll())
                .willReturn(expectedMenuGroups);

        //when
        final List<MenuGroup> actual = menuGroupService.list();

        //then
        assertSoftly(softly -> {
            softly.assertThat(actual.size()).isEqualTo(expectedMenuGroups.size());
            softly.assertThat(actual.get(0)).isEqualTo(expectedMenuGroups.get(0));
            softly.assertThat(actual.get(1)).isEqualTo(expectedMenuGroups.get(1));
        });
    }
}
