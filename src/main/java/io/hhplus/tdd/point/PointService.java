package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final UserPointTable userPointTable;

    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint chargePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint updatedUserPoint = userPoint.charge(amount);
        return userPointTable.insertOrUpdate(id, updatedUserPoint.point());
    }

    public UserPoint usePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint updatedUserPoint = userPoint.use(amount);
        return userPointTable.insertOrUpdate(id, updatedUserPoint.point());
    }
}
