package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.TableFixture;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableCreatable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDeletable;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableCreatable tableCreator;

    @Mock
    private TableGroupDeletable tableEraser;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        tableGroup = TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .build();
        tableGroupRequest = TableFixture.createTableGroupRequest(Arrays.asList(1L, 2L));
    }

    @DisplayName("단체 지정을 등록할 수 있다")
    @Test
    void create() {
        final TableGroup savedTableGroup = TableGroup.builder()
                .of(tableGroup)
                .id(1L)
                .build();
        when(tableGroupRepository.save(any())).thenReturn(savedTableGroup);

        final TableGroupResponse actual = tableGroupService.create(tableGroupRequest);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new TableGroupResponse(savedTableGroup));
    }

    @DisplayName("tableCreator 에서 예외가 발생하면 그룹생성이 되지 않는다")
    @Test
    void createException() {
        when(tableGroupRepository.save(any())).thenReturn(TableFixture.createTableGroup());
        doThrow(IllegalArgumentException.class).when(tableCreator)
                .create(any(), any());

        assertThatThrownBy(() -> tableGroupService.create(any()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        doNothing().when(tableEraser).ungroup(any());

        assertThatCode(() -> tableGroupService.ungroup(any())).doesNotThrowAnyException();
    }

    @DisplayName("tableEraser 에서 예외가 발생하면 그룹 해제가 되지 않는다")
    @Test
    void ungroupExceptionExistsAndStatus() {
        doThrow(IllegalArgumentException.class).when(tableEraser).ungroup(any());

        assertThatThrownBy(() -> tableGroupService.ungroup(any()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
