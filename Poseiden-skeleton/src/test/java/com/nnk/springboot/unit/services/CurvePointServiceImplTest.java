package com.nnk.springboot.unit.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CurvePointServiceImplTest {

    @Mock
    private CurvePointRepository curvePointRepository;
    @InjectMocks
    private CurvePointServiceImpl curvePointServiceUnderTest;

    @Test
    public void getCurvePointsTest() {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();
        List<CurvePoint> dummyCurvePointList = new ArrayList<>(Arrays.asList(dummyCurvePoint,dummyCurvePoint));
        Mockito.when(curvePointRepository.findAll()).thenReturn(dummyCurvePointList);

        List<CurvePoint> curvePoints = curvePointServiceUnderTest.getCurvePoints();

        Assertions.assertThat(curvePoints.size()).isEqualTo(2);
        Assertions.assertThat(curvePoints.get(0)).isEqualTo(dummyCurvePoint);
        Assertions.assertThat(curvePoints.get(1)).isEqualTo(dummyCurvePoint);
    }

    @Test
    public void saveCurvePointTest() {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();

        curvePointServiceUnderTest.saveCurvePoint(dummyCurvePoint);

        Mockito.verify(curvePointRepository, Mockito.times(1)).save(dummyCurvePoint);
    }

    @Test
    public void getCurvePointByIdTest() {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();
        Mockito.when(curvePointRepository.findById(1)).thenReturn(Optional.of(dummyCurvePoint));

        CurvePoint curvePoint = curvePointServiceUnderTest.getCurvePointById(1).get();

        Assertions.assertThat(curvePoint).isEqualTo(dummyCurvePoint);
    }

    @Test
    public void deleteCurvePointTest() {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();

        curvePointServiceUnderTest.deleteCurvePoint(dummyCurvePoint);

        Mockito.verify(curvePointRepository, Mockito.times(1)).delete(dummyCurvePoint);
    }

    public CurvePoint getDummyCurvePoint() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(10);
        curvePoint.setTerm(10d);
        curvePoint.setValue(30d);
        return curvePoint;
    }
}