package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.dto.MenuGroupRequest;
import kitchenpos.domain.dto.MenuGroupResponse;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.MenuGroupBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private static Stream<List<MenuGroup>> should_return_menuGroup_list_when_request_list() {
        final MenuGroup menuGroup1 = new MenuGroupBuilder().build();
        final MenuGroup menuGroup2 = new MenuGroupBuilder().build();
        final MenuGroup menuGroup3 = new MenuGroupBuilder().build();

        return Stream.of(
                List.of(),
                List.of(menuGroup1),
                List.of(menuGroup2, menuGroup3)
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("모든 메뉴 그룹 목록을 조회할 수 있다.")
    void should_return_menuGroup_list_when_request_list(final List<MenuGroup> newMenuGroups) {
        // given
        menuGroupRepository.saveAll(newMenuGroups);

        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        final List<MenuGroupResponse> expect = menuGroups.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());

        // when
        final List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void should_save_new_menuGroup_when_create() {
        // given
        final MenuGroupRequest menuGroup = new MenuGroupRequest("메뉴 그룹 이름");

        // when
        final MenuGroupResponse expect = menuGroupService.create(menuGroup);

        // then
        final MenuGroup savedMenuGroup = menuGroupRepository.findById(expect.getId()).get();

        final MenuGroupResponse actual = MenuGroupResponse.from(savedMenuGroup);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }
}
