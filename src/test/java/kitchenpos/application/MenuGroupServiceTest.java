package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 서비스")
class MenuGroupServiceTest {
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Nested
    @DisplayName("생성 메서드는")
    class CreateMenuGroup {
        private MenuGroup request;

        private MenuGroup subject() {
            return menuGroupService.create(request);
        }

        @Nested
        @DisplayName("메뉴 그룹명이 주어지면")
        class WithName {
            @BeforeEach
            void setUp() {
                request = createMenuGroupRequest("추천메뉴");

                given(menuGroupDao.save(any(MenuGroup.class))).willAnswer(i -> {
                    MenuGroup saved = i.getArgument(0, MenuGroup.class);
                    saved.setId(1L);
                    return saved;
                });
            }

            @Test
            @DisplayName("메뉴 그룹을 생성한다")
            void createMenuGroups() {
                MenuGroup result = subject();

                assertAll(
                        () -> assertThat(result).isEqualToIgnoringGivenFields(request, "id"),
                        () -> assertThat(result.getId()).isNotNull()
                );
            }
        }
    }

    @Nested
    @DisplayName("조회 메서드는")
    class FindMenuGroup {
        private List<MenuGroup> subject() {
            return menuGroupService.list();
        }

        @Nested
        @DisplayName("메뉴 그룹이 저장되어 있으면")
        class WithName {
            private List<MenuGroup> menuGroups;

            @BeforeEach
            void setUp() {
                menuGroups = Arrays.asList(
                        createMenuGroup(1L, "추천 메뉴"),
                        createMenuGroup(2L, "맛잇는 메뉴")
                );

                given(menuGroupDao.findAll()).willReturn(menuGroups);
            }

            @Test
            @DisplayName("메뉴 그룹을 조회한다")
            void findMenuGroups() {
                List<MenuGroup> result = subject();

                assertThat(result).usingDefaultComparator().isEqualTo(menuGroups);
            }
        }
    }
}