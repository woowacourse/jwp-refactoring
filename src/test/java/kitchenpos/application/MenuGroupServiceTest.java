package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuGroupFakeDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService = new MenuGroupService(new MenuGroupFakeDao());

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        final var korean = menuGroup("한식");

        final var result = menuGroupService.create(korean);

        assertThat(result.getName()).isEqualTo(korean.getName());
    }

    @DisplayName("모든 메뉴 그룹 조회")
    @Test
    void list() {
        final var korean = menuGroup("한식");
        final var japanese = menuGroup("일식");

        menuGroupService.create(korean);
        menuGroupService.create(japanese);

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
