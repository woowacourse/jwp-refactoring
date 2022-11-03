package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest extends FakeSpringContext {

    private final MenuGroupService menuGroupService = new MenuGroupService(menuGroups);

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        final var korean = new MenuGroupRequest("한식");

        final var result = menuGroupService.create(korean);

        assertThat(result.getName()).isEqualTo(korean.getName());
    }

    @DisplayName("모든 메뉴 그룹 조회")
    @Test
    void list() {
        final var korean = menuGroupDao.save(menuGroup("한식"));
        final var japanese = menuGroupDao.save(menuGroup("일식"));

        final var result = menuGroupService.list();
        final var foundKorean = findMenuGroupInList(result, korean);
        final var foundJapanese = findMenuGroupInList(result, japanese);

        assertAll(
                () -> assertThat(foundKorean).isPresent(),
                () -> assertThat(foundJapanese).isPresent()
        );
    }

    private Optional<MenuGroup> findMenuGroupInList(final List<MenuGroup> result, final MenuGroup korean) {
        return result.stream()
                .filter(menuGroup -> menuGroup.getName().equals(korean.getName()))
                .findAny();
    }
}
