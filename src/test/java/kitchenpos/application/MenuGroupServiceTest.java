package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuGroupServiceTest extends AbstractServiceTest {
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = createMenuGroupRequest("메뉴그룹1");

        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupCreateRequest);

        assertAll(
            () -> assertThat(savedMenuGroup.getId()).isNotNull(),
            () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupCreateRequest.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<MenuGroupResponse> savedMenuGroups = MenuGroupResponse.listOf(Arrays.asList(
            menuGroupRepository.save(createMenuGroup(null, "메뉴그룹1")),
            menuGroupRepository.save(createMenuGroup(null, "메뉴그룹2")),
            menuGroupRepository.save(createMenuGroup(null, "메뉴그룹3"))
        ));

        List<MenuGroupResponse> allMenuGroups = menuGroupService.list();

        assertThat(allMenuGroups).usingFieldByFieldElementComparator().containsAll(savedMenuGroups);
    }
}
