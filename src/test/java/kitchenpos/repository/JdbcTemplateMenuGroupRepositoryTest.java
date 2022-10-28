package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateMenuGroupRepositoryTest {

    private final MenuGroupRepository menuGroupRepository;

    @Autowired
    public JdbcTemplateMenuGroupRepositoryTest(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Test
    void 저장한다() {
        // given
        MenuGroup menuGroup = new MenuGroup("menu-group");

        // when
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        // then
        Assertions.assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo("menu-group")
        );
    }

    @Test
    void ID로_조회한다() {
        // given
        Long id = 1L;

        // when
        Optional<MenuGroup> menuGroup = menuGroupRepository.findById(id);

        // then
        Assertions.assertAll(
                () -> assertThat(menuGroup).isPresent(),
                () -> assertThat(menuGroup.get().getName()).isEqualTo("두마리메뉴")
        );
    }

    @Test
    void 일치하는_ID가_없는_경우_empty를_반환한다() {
        // given
        Long notExistId = -1L;

        // when
        Optional<MenuGroup> foundMenuGroup = menuGroupRepository.findById(notExistId);

        // then
        assertThat(foundMenuGroup).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        // then
        assertThat(menuGroups).hasSize(4)
                .usingRecursiveComparison()
                .isEqualTo(
                        Arrays.asList(
                                new MenuGroup(1L, "두마리메뉴"),
                                new MenuGroup(2L, "한마리메뉴"),
                                new MenuGroup(3L, "순살파닭두마리메뉴"),
                                new MenuGroup(4L, "신메뉴")
                        )
                );
    }

    @ParameterizedTest
    @CsvSource(value = {"1,true", "100,false"})
    void ID에_맞는_데이터가_존재하는지_확인한다(Long id, boolean expected) {
        // given
        boolean actual = menuGroupRepository.existsById(id);

        // when & then
        assertThat(actual).isEqualTo(expected);
    }
}
