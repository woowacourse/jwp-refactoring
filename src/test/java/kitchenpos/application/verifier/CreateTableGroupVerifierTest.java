package kitchenpos.application.verifier;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
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
import kitchenpos.domain.model.tablegroup.TableGroup;

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
                dynamicTest("요청한 테이블들이 하나씩 존재하지 않을때 IllegalArgumentException 발생",
                        this::orderTableMismatch),
                dynamicTest("테이블에 손님이 있을때 IllegalArgumentException 발생", this::orderTableNotEmpty)
        );
    }

    private void createSuccess() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, 0, true),
                        new OrderTable(2L, 0, true)));

        assertDoesNotThrow(() -> createTableGroupVerifier.toTableGroup(tableGroup.orderTableIds()));
    }

    private void orderTableMismatch() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(singletonList(new OrderTable(1L, 0, true)));

        throwIllegalArgumentException(tableGroup);
    }

    private void orderTableNotEmpty() {
        TableGroup tableGroup = createTableGroup();

        given(orderTableRepository.findAllByIdIn(tableGroup.orderTableIds()))
                .willReturn(asList(new OrderTable(1L, 0, false),
                        new OrderTable(2L, 0, true)));

        throwIllegalArgumentException(tableGroup);
    }

    private TableGroup createTableGroup() {
        List<OrderTable> orderTables = TABLE_GROUP_CREATE_REQUEST.getOrderTables().stream()
                .map(id -> new OrderTable(id, 0, false))
                .collect(toList());
        return new TableGroup(null, orderTables, null);
    }

    private void throwIllegalArgumentException(TableGroup tableGroup) {
        assertThatIllegalArgumentException()
                .isThrownBy(
                        () -> createTableGroupVerifier.toTableGroup(tableGroup.orderTableIds()));
    }
}
