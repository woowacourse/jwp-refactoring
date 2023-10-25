package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹을_등록한다 {
        @Test
        void 메뉴_그룹이_정상적으로_등록된다() {
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("메뉴그룹");
            final MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupCreateRequest);

            assertThat(menuGroupResponse)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menuGroupCreateRequest);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 255})
        void 메뉴_그룹_이름은_255자_이하이다(int length) {
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("메".repeat(length));
            final MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupCreateRequest);

            assertThat(menuGroupResponse)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(menuGroupCreateRequest);
        }

        @Test
        void 메뉴_그룹_이름이_256자_이상이면_예외가_발생한다() {
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("메".repeat(256));

            assertThatThrownBy(() -> menuGroupService.create(menuGroupCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹_이름이_없으면_예외가_발생한다() {
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(null);

            assertThatThrownBy(() -> menuGroupService.create(menuGroupCreateRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 메뉴_그룹의_목록을_조회한다() {
        final List<MenuGroupResponse> expected = menuGroupService.list();
        for (int i = 0; i < 3; i++) {
            final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("메뉴그룹" + i);
            expected.add(menuGroupService.create(menuGroup));
        }

        final List<MenuGroupResponse> result = menuGroupService.list();

        assertThat(result).hasSize(expected.size());
    }
}
