package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다")
    @Test
    void create() {
        // given
        final var expectedName = "순살 두 마리 메뉴";
        final var menuGroupRequest = new MenuGroup(expectedName);

        // when
        final var actual = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo(expectedName)
        );
    }

    @DisplayName("MenuGroupService의 create메서드는")
    @Nested
    class MenuGroupCreate {
        @DisplayName("name이 Null 또는 빈 문자열일 경우 예외가 발생한다")
        @NullAndEmptySource
        @ParameterizedTest
        void should_fail_on_null_or_empty_string(final String nullOrEmptyString) {
            // given
            final var menuGroupRequest = new MenuGroup(nullOrEmptyString);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuGroupService.create(menuGroupRequest)
            );
        }

        @DisplayName("name이 공백으로만 이루어진 문자열일 경우 예외가 발생한다")
        @Test
        void should_fail_on_string_with_white_spaces() {
            // given
            final var menuGroupRequest = new MenuGroup("    ");

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> menuGroupService.create(menuGroupRequest)
            );
        }
    }

    @DisplayName("전체 메뉴 그룹을 조회할 수 있다")
    @Test
    void list() {
        // given
        final var name1 = "순살 한 마리";
        final var name2 = "뼈 한 마리";
        menuGroupService.create(new MenuGroup(name1));
        menuGroupService.create(new MenuGroup(name2));

        // when
        final var actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual)
                        .extracting("name")
                        .containsExactly(name1, name2)
        );
    }
}
