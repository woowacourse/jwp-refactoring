package kitchenpos.application;

import kitchenpos.application.fixture.MenuGroupServiceFixture;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends MenuGroupServiceFixture {

    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupDao menuGroupDao;

    @Test
    void 메뉴를_등록한다() {
        // given
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(저장된_메뉴_그룹);

        final MenuGroup menuGroup = new MenuGroup(메뉴_그룹_이름);

        // when
        final MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo(menuGroup.getName());
        });
    }

    @Test
    void 메뉴_목록을_조횐한다() {
        // given
        given(menuGroupDao.findAll()).willReturn(저장된_메뉴_그룹들);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(저장된_메뉴_그룹1);
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(저장된_메뉴_그룹2);
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison()
                          .isEqualTo(저장된_메뉴_그룹3);
        });
    }
}
