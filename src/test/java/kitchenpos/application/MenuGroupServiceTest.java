package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends IntegrationTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    @DisplayName("메뉴 그룹 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_그룹_등록_성공_저장() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중식");

        // when
        final MenuGroup saved = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroupService.list())
                .map(MenuGroup::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("메뉴 그룹 등록 시 이름이 있어야 한다.")
    void 메뉴_그룹_등록_실패_이름_없음() {
        // given
        // when
        final MenuGroup menuGroup = new MenuGroup();

        // then
        /// TODO: 2023/10/12 DB 가기 전에 예외처리하기
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(Exception.class);
    }
}
