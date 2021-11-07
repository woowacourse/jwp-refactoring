package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ServiceTest
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao mockMenuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        MenuGroupRequest menuGroupRequest = createMenuGroupRequest();
        when(mockMenuGroupDao.save(any())).thenReturn(menuGroupRequest.toEntity(1L));
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);
        assertAll(
                () -> assertThat(savedMenuGroup).isNotNull(),
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupRequest.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 반환한다.")
    @Test
    void list() {
        List<MenuGroup> savedMenuGroups = Arrays.asList(createMenuGroup(), createMenuGroup());
        when(mockMenuGroupDao.findAll()).thenReturn(savedMenuGroups);

        List<MenuGroupResponse> list = menuGroupService.list();
        assertAll(
                () -> assertThat(list).hasSize(savedMenuGroups.size()),
                () -> assertThat(MenuGroupResponse.listOf(savedMenuGroups)).usingRecursiveComparison().isEqualTo(list)
        );
    }
}
