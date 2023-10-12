package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹을_등록한다 {
        @Test
        void 메뉴_그룹이_정상적으로_등록된다() {
            final MenuGroup menuGroup = new MenuGroup(null, "메뉴그룹");
            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            assertThat(savedMenuGroup)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menuGroup);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 255})
        void 메뉴_그룹_이름은_255자_이하이다(int length) {
            final MenuGroup menuGroup = new MenuGroup(null, "메".repeat(length));
            final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            assertThat(savedMenuGroup)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menuGroup);
        }

        @Test
        void 메뉴_그룹_이름이_256자_이상이면_예외가_발생한다() {
            final MenuGroup menuGroup = new MenuGroup(null, "메".repeat(256));

            assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹_이름이_없으면_예외가_발생한다() {
            final MenuGroup menuGroup = new MenuGroup(null, null);

            assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴_그룹의_목록을_조회한다() {
        final List<MenuGroup> expected = menuGroupService.list();
        for (int i = 0; i < 3; i++) {
            final MenuGroup menuGroup = new MenuGroup(null, "메뉴그룹" + i);
            expected.add(menuGroupService.create(menuGroup));
        }

        final List<MenuGroup> result = menuGroupService.list();

        assertSoftly(softAssertions -> {
            assertThat(result).hasSize(expected.size());
            assertThat(result)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        });
    }
}
