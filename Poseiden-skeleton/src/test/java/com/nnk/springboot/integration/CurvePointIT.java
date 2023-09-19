package com.nnk.springboot.integration;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class CurvePointIT {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        curvePointRepository.deleteAll();
       jdbcTemplate.execute("TRUNCATE TABLE curvepoint RESTART IDENTITY;");
        // Le script SQL réinitialise l'incrémentation de l'id dans la table
        // Sans ça les "findById" déconnent

    }
    @DisplayName("validateForm_WithValidCurvePoint_ShouldSaveCurvePointToDatabase")
    @Test
    @WithMockUser(authorities = "USER")
    public void createOneCurvePoint() throws Exception {
        CurvePoint dummyCurvePoint1 = getDummyCurvePoint1();

        mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                .param("curveId",String.valueOf(dummyCurvePoint1.getCurveId()))
                .param("term",String.valueOf(dummyCurvePoint1.getTerm()))
                .param("value",String.valueOf(dummyCurvePoint1.getValue())));


        CurvePoint curvePoint = curvePointRepository.findById(dummyCurvePoint1.getId()).get();

        Assertions.assertThat(curvePoint.getId()).isNotNull();
        Assertions.assertThat(curvePoint.getCurveId()).isEqualTo(dummyCurvePoint1.getCurveId());
        Assertions.assertThat(curvePoint.getTerm()).isEqualTo(dummyCurvePoint1.getTerm());
        Assertions.assertThat(curvePoint.getValue()).isEqualTo(dummyCurvePoint1.getValue());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void showCurvePointHome_WithCurvePointsSaved_ShouldDisplayCurvePointsFromDatabase() throws Exception {
        createTwoCurvePoints();
        CurvePoint dummyCurvePoint1 = getDummyCurvePoint1();
        CurvePoint dummyCurvePoint2 = getDummyCurvePoint2();

        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(String.valueOf(dummyCurvePoint1.getCurveId()))))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(String.valueOf(dummyCurvePoint2.getCurveId()))));
    }


    @Test
    @WithMockUser(authorities = "USER")
    public void UpdateCurvePoint_WithValidCurvePoint_ShouldSaveCurvePointToDatabase() throws Exception {
        createOneCurvePoint();
        CurvePoint curvePoint = curvePointRepository.findById(1).get();

        mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .param("curveId",String.valueOf(curvePoint.getCurveId()))
                        .param("term",String.valueOf(curvePoint.getTerm()))
                        .param("value","8"));

        CurvePoint curvePointUpdate = curvePointRepository.findById(1).get();
        Assertions.assertThat(curvePoint.getValue()).isNotEqualTo(8);
        Assertions.assertThat(curvePointUpdate.getValue()).isEqualTo(8);
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void deleteCurvePoint_ShouldDeleteCurvePointToDatabase() throws Exception {
        createOneCurvePoint();
        Optional<CurvePoint> curvePoint = curvePointRepository.findById(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"));

        Optional <CurvePoint> curvePointDeleted = curvePointRepository.findById(1);

        Assertions.assertThat(curvePoint.isPresent()).isTrue();
        Assertions.assertThat(curvePointDeleted.isEmpty()).isTrue();
    }

    public void createTwoCurvePoints() throws Exception {
        createOneCurvePoint();
        CurvePoint curvePoint2 = getDummyCurvePoint2();
        curvePointRepository.save(curvePoint2);
    }

    public CurvePoint getDummyCurvePoint1() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setCurveId(11);
        curvePoint.setTerm(11d);
        curvePoint.setValue(31d);
        return curvePoint;
    }

    public CurvePoint getDummyCurvePoint2() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(2);
        curvePoint.setCurveId(12);
        curvePoint.setTerm(12d);
        curvePoint.setValue(32d);
        return curvePoint;
    }
}