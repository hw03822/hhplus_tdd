package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.*;

class PointServiceTest {
    @Test
    @DisplayName("포인트 충전 시 DB조회 및 저장 호출을 검증한다")
    void chargePoint_VerifyProcess() {
        //given
        long id = 1L;
        long amount = 200L;

        // DB Mock
        UserPointTable mockRepository = mock(UserPointTable.class);

        // Domain Mock
        // 조회된 객체 생성
        UserPoint mockUserPoint = mock(UserPoint.class);
        // 포인트 충전 후 반환될 객체
        UserPoint mockUserPointAfter = mock(UserPoint.class);

        // service 설정
        PointService pointService = new PointService(mockRepository);

        // selectById 호출 -> mockUserPoint 반환
        when(mockRepository.selectById(id)).thenReturn(mockUserPoint);

        // charge 호출 -> mockUserPointAfter 반환
        when(mockUserPoint.charge(amount)).thenReturn(mockUserPointAfter);

        // insertOrUpdate 호출 -> mockUserPointAfter 반환
        when(mockRepository.insertOrUpdate(id, mockUserPointAfter.point()))
                .thenReturn(mockUserPointAfter);

        //when
        UserPoint result = pointService.chargePoint(id, amount);

        //then : 흐름 검증 (흐름 검증이라 값에 대한 검증은 하지 않음)
        // selectById 가 호출 됐는지
        verify(mockRepository).selectById(id);

        // use 가 호출 됐는지
        verify(mockUserPoint).charge(amount);

        // insertOrUpdate 가 호출 됐는지
        verify(mockRepository).insertOrUpdate(id, mockUserPointAfter.point());
    }

    @Test
    @DisplayName("포인트 충전값이 0보다 작을 때 예외 처리를 검증한다.")
    void chargePoint_VerifyException() {
        //given
        long id = 1L;
        long amount = -100L;

        //Mock 생성
        UserPointTable mockRepository = mock(UserPointTable.class);
        UserPoint mockUserPoint = mock(UserPoint.class);

        PointService pointService = new PointService(mockRepository);

        //selectById 호출 -> mockUserPoint 반환
        when(mockRepository.selectById(id)).thenReturn(mockUserPoint);

        //charge 호출 -> 예외 반환
        when(mockUserPoint.charge(amount)).thenThrow(new IllegalArgumentException("0이상 충전 가능"));

        //when

        //then
        assertThatThrownBy(() -> pointService.chargePoint(id,amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0이상 충전 가능");
    }
}