package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends IntegrationTestHelper {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroup menuGroup = 메뉴그룹_생성();

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroup.getName()).isEqualTo(result.getName());
    }

    @Test
    void 모두_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupService.create(메뉴그룹_생성());

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(menuGroup);
        });
    }
}
