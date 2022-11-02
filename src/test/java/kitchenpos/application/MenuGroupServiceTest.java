package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixtures.메뉴_그룹_목록_조회;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService sut;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroup() {
        final MenuGroupRequest request = new MenuGroupRequest("떡잎 유치원");

        final MenuGroup actual = sut.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(request.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void getMenuGroups() {
        final List<MenuGroup> menuGroups = 메뉴_그룹_목록_조회();

        assertThat(sut.list())
                .hasSize(4)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(menuGroups);
    }
}
