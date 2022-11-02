package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.menugroup.MenuGroupRequest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.support.application.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class MenuGroupServiceTest {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(final MenuGroupRepository menuGroupRepository,
                                final MenuGroupService menuGroupService
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("메뉴 그룹을 추가한다")
    @Test
    void create() {
        final var request = new MenuGroupRequest("양식");
        final var actual = menuGroupService.create(request);
        assertThat(actual.getId()).isPositive();
    }

    @DisplayName("메뉴 그룹을 전체 조회한다")
    @Test
    void list() {
        final var expectedSize = 4;
        saveMenuGroupAsTimes(expectedSize);

        final var actual = menuGroupService.list();
        assertThat(actual).hasSize(expectedSize);
    }

    private void saveMenuGroupAsTimes(final int times) {
        for (int i = 0; i < times; i++) {
            final var menuGroup = new MenuGroup("메뉴 그룹" + i);
            menuGroupRepository.save(menuGroup);
        }
    }
}
