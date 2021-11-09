package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuGroupService 테스트")
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Test
        @DisplayName("MenuGroup을 반환한다.")
        void it_return_menuGroup() {
            // given
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("치킨");
            given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

            // when
            MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

            // then
            assertThat(createdMenuGroup).isEqualTo(menuGroup);
            then(menuGroupDao)
                    .should(times(1))
                    .save(menuGroup);
        }
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Test
        @DisplayName("MenuGroup 리스트를 반환한다.")
        void it_return_menuGroup_list() {
            // given
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("치킨");
            MenuGroup anotherMenuGroup = new MenuGroup();
            anotherMenuGroup.setName("피자");
            given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup, anotherMenuGroup));

            // when
            List<MenuGroup> menuGroups = menuGroupService.list();

            // then
            assertThat(menuGroups).containsExactly(menuGroup, anotherMenuGroup);
            assertThat(menuGroups).hasSize(2);
            then(menuGroupDao)
                    .should(times(1))
                    .findAll();
        }
    }
}