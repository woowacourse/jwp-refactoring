package kitchenpos.tablegroup.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class TableGroupVerifier {
    private static final int MINIMUM_TABLE_COUNT = 2;

    public static void validate(List<Long> orderTableIds, int savedSize) {
        validateMinimumTableCount(orderTableIds);
        validateTableExistence(orderTableIds, savedSize);
    }

    private static void validateMinimumTableCount(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MINIMUM_TABLE_COUNT) {
            throw new IllegalArgumentException("그룹 지정은 테이블의 수가 2보다 커야 합니다.");
        }
    }

    private static void validateTableExistence(List<Long> orderTableIds, int savedSize) {
        if (orderTableIds.size() != savedSize) {
            throw new IllegalArgumentException("존재하지 않는 테이블은 그룹지정할 수 없습니다.");
        }
    }
}
