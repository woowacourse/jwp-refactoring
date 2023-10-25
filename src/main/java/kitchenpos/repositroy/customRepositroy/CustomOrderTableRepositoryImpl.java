package kitchenpos.repositroy.customRepositroy;

import static kitchenpos.domain.table.QOrderTable.orderTable;
import static kitchenpos.domain.table_group.QTableGroup.tableGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kitchenpos.domain.table.OrderTable;

public class CustomOrderTableRepositoryImpl implements CustomOrderTableRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomOrderTableRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<OrderTable> findAllByFetch() {
        return jpaQueryFactory
                .selectFrom(orderTable)
                .leftJoin(orderTable.tableGroup, tableGroup)
                .fetchJoin()
                .fetch();
    }
}
