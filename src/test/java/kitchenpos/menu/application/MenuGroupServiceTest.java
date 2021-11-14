package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.menu.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        MenuGroupRequest request = createMenuGroupRequest();
        MenuGroupResponse result = menuGroupService.create(request);
        assertSoftly(it -> {
            it.assertThat(result).isNotNull();
            it.assertThat(result.getId()).isNotNull();
            it.assertThat(result.getName()).isEqualTo(request.getName());
        });
    }

    @DisplayName("메뉴 그룹 목록을 반환한다.")
    @Test
    void list() {
        List<MenuGroup> saved = menuGroupRepository.saveAll(Arrays.asList(createMenuGroup(), createMenuGroup()));
        List<MenuGroupResponse> result = menuGroupService.list();
        assertSoftly(it -> {
            it.assertThat(result).hasSize(saved.size());
            it.assertThat(MenuGroupResponse.listOf(saved)).usingRecursiveComparison().isEqualTo(result);
        });
    }

    @AfterEach
    void tearDown() {
        menuGroupRepository.deleteAll();
    }
}
