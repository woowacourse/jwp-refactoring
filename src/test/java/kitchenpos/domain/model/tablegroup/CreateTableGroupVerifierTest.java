package kitchenpos.domain.model.tablegroup;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class CreateTableGroupVerifierTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private CreateTableGroupVerifier createTableGroupVerifier;

    @DisplayName("단체 지정 생성")
    @TestFactory
    Stream<DynamicTest> create() {
        return Stream.of(
                dynamicTest("단체 지정을 생성한다.", this::createSuccess),
                dynamicTest("요청한 테이블들이 하나씩 존재해야 한다.", this::orderTableMismatch),
                dynamicTest("테이블에 손님이 없어야 한다.", this::orderTableNotEmpty),
                dynamicTest("단체 지정이 되어있지 않아야 한다.", this::orderTableHasTableGroup)
        );
    }

    private void createSuccess() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, null, 0, true),
                        new OrderTable(2L, null, 0, true)));

        assertDoesNotThrow(() -> createTableGroupVerifier.toTableGroup(tableGroup.orderTableIds()));
    }

    private void orderTableMismatch() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(singletonList(new OrderTable(1L, null, 0, true)));

        throwIllegalArgumentException(tableGroup);
    }

    private void orderTableNotEmpty() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, null, 0, false),
                        new OrderTable(2L, null, 0, true)));

        throwIllegalArgumentException(tableGroup);
    }

    private void orderTableHasTableGroup() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, 1L, 0, true),
                        new OrderTable(2L, null, 0, true)));

        throwIllegalArgumentException(tableGroup);
    }

    private TableGroup createTableGroup() {
        return new TableGroup(null, TABLE_GROUP_CREATE_REQUEST.getOrderTables(), null);
    }

    private void throwIllegalArgumentException(TableGroup tableGroup) {
        assertThatIllegalArgumentException()
                .isThrownBy(
                        () -> createTableGroupVerifier.toTableGroup(tableGroup.orderTableIds()));
    }
}
