package kitchenpos.application;

import static kitchenpos.fixtures.TestFixtures.메뉴_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("SERVICE_MenuGroupService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 메뉴_그룹이_입력되면 extends ServiceTest {

            private final MenuGroupCreateRequest request = 메뉴_그룹_생성_요청("한마리메뉴");

            @Test
            void 해당_메뉴_그룹을_반환한다() {
                final MenuGroupResponse actual = menuGroupService.create(request);

                assertThat(actual).isNotNull();
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되면 extends ServiceTest {

            @Test
            void 모든_메뉴_그룹을_반환한다() {
                final List<MenuGroupResponse> actual = menuGroupService.list();

                assertThat(actual).isEmpty();
            }
        }
    }
}
