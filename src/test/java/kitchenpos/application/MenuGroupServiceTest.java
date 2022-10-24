package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        final var korean = new MenuGroup("한식");

        final var result = menuGroupService.create(korean);

        assertThat(result).isEqualTo(korean);
    }

    @DisplayName("모든 메뉴 그룹 조회")
    @Test
    void list() {
        final var existingMenuGroups = menuGroupService.list();

        final var korean = new MenuGroup("한식");
        final var japanese = new MenuGroup("일식");

        menuGroupService.create(korean);
        menuGroupService.create(japanese);

        final var result = menuGroupService.list();
        final var filteredResult = filterNewlyCreated(existingMenuGroups, result);
        assertThat(filteredResult).containsExactly(korean, japanese);
    }

    private List<MenuGroup> filterNewlyCreated(final List<MenuGroup> olds, final List<MenuGroup> news) {
        return news.stream()
                .filter(menuGroup -> !olds.contains(menuGroup))
                .collect(Collectors.toList());
    }
}
