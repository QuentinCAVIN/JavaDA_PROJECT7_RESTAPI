package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointServiceImpl implements CurvePointService {
    @Autowired
    CurvePointRepository curvePointRepository;
    public List<CurvePoint> getCurvePoints(){
        return curvePointRepository.findAll();
    }

    public void saveCurvePoint (CurvePoint curvePoint){
        curvePointRepository.save(curvePoint);
    }

    public Optional<CurvePoint> getCurvePointById (int id) {
        return curvePointRepository.findById(id);
    }

    public void deleteCurvePoint(CurvePoint curvePoint) {
        curvePointRepository.delete(curvePoint);
    }
}
