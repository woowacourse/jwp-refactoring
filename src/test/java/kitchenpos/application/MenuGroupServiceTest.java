package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import support.fixture.MenuGroupBuilder;

import java.util.List;
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
    void should_return_menuGroup_list_when_request_list(final List<MenuGroup> menuGroups) {
        // given
        final List<MenuGroup> expect = menuGroupRepository.findAll();
        expect.addAll(menuGroups);

        menuGroupRepository.saveAll(menuGroups);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expect);
    }

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void should_save_new_menuGroup_when_create() {
        // given
        final MenuGroup menuGroup = new MenuGroupBuilder().build();

        // when
        final MenuGroup expect = menuGroupService.create(menuGroup);

        // then
        final MenuGroup actual = menuGroupRepository.findById(expect.getId()).get();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expect);
    }
}
