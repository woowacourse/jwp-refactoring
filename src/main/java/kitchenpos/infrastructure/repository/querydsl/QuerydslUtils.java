package kitchenpos.infrastructure.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.function.Supplier;

public class QuerydslUtils {

    private QuerydslUtils() {
    }

    public static BooleanBuilder nullSafeBuilder(final Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }
}
