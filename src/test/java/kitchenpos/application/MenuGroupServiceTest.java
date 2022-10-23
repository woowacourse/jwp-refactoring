package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;
    private final MenuGroupDao menuGroupDao;

    @Autowired
    public MenuGroupServiceTest(final MenuGroupService menuGroupService, MenuGroupDao menuGroupDao) {
        this.menuGroupService = menuGroupService;
        this.menuGroupDao = menuGroupDao;
    }

    @Test
    void create() {
        final var expected = new MenuGroup("양식");
        final var actual = menuGroupService.create(expected);

        assertThat(actual.getId()).isPositive();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void list() {
        final List<MenuGroup> expected = Stream.of("한식", "양식", "중식", "일식")
                .map(MenuGroup::new)
                .map(menuGroupDao::save)
                .collect(Collectors.toUnmodifiableList());
        final List<MenuGroup> actual = menuGroupService.list();

        assertAllMatches(actual, expected);
    }

    private void assertAllMatches(final List<MenuGroup> actualList, final List<MenuGroup> expectedList) {
        final var expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);

            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }
}