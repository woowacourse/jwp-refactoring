package kitchenpos.module.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import kitchenpos.module.IntegrationTest;
import kitchenpos.module.domain.menu.MenuGroup;
import kitchenpos.module.presentation.dto.request.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_그룹_등록_성공_저장() {
        // given
        final MenuGroup saved = menuGroupService.create(new MenuGroupCreateRequest("중식"));

        // expected
        assertThat(menuGroupService.list())
                .map(MenuGroup::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }
}
