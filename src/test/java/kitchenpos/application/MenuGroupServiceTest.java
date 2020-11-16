package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.repository.MenuGroupRepository;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("Menu Group 생성")
    @Test
    void create() {
        MenuGroupCreateRequest request = MenuGroupFixture.createRequest();
        MenuGroup menuGroupWithId = MenuGroupFixture.createWithId(1L);

        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(menuGroupWithId);

        assertThat(menuGroupService.create(request))
            .isEqualToComparingFieldByField(menuGroupWithId);
    }

    @DisplayName("Menu Group 조회")
    @Test
    void list() {
        MenuGroup menuGroupWithId1 = MenuGroupFixture.createWithId(1L);
        MenuGroup menuGroupWithId2 = MenuGroupFixture.createWithId(2L);
        when(menuGroupRepository.findAll()).thenReturn(
            Arrays.asList(menuGroupWithId1, menuGroupWithId2));

        assertThat(menuGroupService.list())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(menuGroupWithId1, menuGroupWithId2));
    }
}
