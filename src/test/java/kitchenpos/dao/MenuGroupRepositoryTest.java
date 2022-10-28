package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("MenuGroup 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class save_메서드는 {

        @Nested
        class 메뉴_그룹이_주어지면 {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");

            @Test
            void 저장한다() {
                final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

                assertThat(savedMenuGroup.getId()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        @Nested
        class id가_주어지면 {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");
            private MenuGroup savedMenuGroup;

            @BeforeEach
            void setUp() {
                savedMenuGroup = menuGroupRepository.save(menuGroup);
            }

            @Test
            void 해당하는_MenuGroup을_반환한다() {
                final Optional<MenuGroup> foundMenuGroup = menuGroupRepository.findById(savedMenuGroup.getId());

                assertAll(
                        () -> assertThat(foundMenuGroup).isPresent(),
                        () -> assertThat(foundMenuGroup.get()).usingRecursiveComparison()
                                .isEqualTo(savedMenuGroup)
                );
            }
        }
    }


    @Nested
    class findAll_메서드는 {

        @Nested
        class 호출되면 {

            private final MenuGroup menuGroup = new MenuGroup("세마리메뉴");
            private MenuGroup savedMenuGroup;

            @BeforeEach
            void setUp() {
                savedMenuGroup = menuGroupRepository.save(menuGroup);
            }

            @Test
            void 모든_MenuGroup들을_반환한다() {
                final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

                assertThat(menuGroups).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedMenuGroup));
            }
        }
    }

    @Nested
    class existsById_메서드는 {

        private MenuGroup menuGroup;

        @BeforeEach
        void setUp() {
            this.menuGroup = menuGroupRepository.save(new MenuGroup("세마리메뉴"));
        }

        @Nested
        class 만약_존재하는_메뉴_그룹이라면 {

            @Test
            void true를_반환한다() {
                final boolean actual = menuGroupRepository.existsById(menuGroup.getId());
                assertThat(actual).isTrue();
            }
        }

        @Nested
        class 만약_존재하지_않는_그룹이라면 {

            @Test
            void false를_반환한다() {
                final boolean actual = menuGroupRepository.existsById(Long.MAX_VALUE);
                assertThat(actual).isFalse();
            }
        }
    }
}
