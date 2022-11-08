package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.menu.application.request.MenuGroupRequest;
import kitchenpos.menu.application.response.MenuGroupResponse;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuGroupServiceTest {

    private MenuGroupService sut;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        sut = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("두마리메뉴");
        final MenuGroup menuGroup = new MenuGroup("두마리메뉴");

        // when
        final MenuGroupResponse response = sut.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        final MenuGroup foundMenuGroup = menuGroupRepository.findById(response.getId()).get();
        assertThat(foundMenuGroup)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // when
        final List<MenuGroupResponse> menuGroups = sut.list();

        // then
        assertThat(menuGroups)
                .hasSize(4)
                .extracting(MenuGroupResponse::getName)
                .containsExactlyInAnyOrder(
                        "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"
                );
    }
}
