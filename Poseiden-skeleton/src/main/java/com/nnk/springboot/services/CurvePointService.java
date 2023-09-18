package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;
import java.util.Optional;

public interface CurvePointService {
    List<CurvePoint> getCurvePoints();

    void saveCurvePoint(CurvePoint curvePoint);

    Optional<CurvePoint> getCurvePointById(int id);

    void deleteCurvePoint(CurvePoint curvePoint);
}