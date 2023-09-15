package com.nnk.springboot.unit.controllers;

import com.nnk.springboot.controllers.CurvePointController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CurvePointController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CurvePointControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CurvePointService curvePointService;


    @Test
    @WithMockUser
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoints"));
    }

    @Test
    public void addCurvePointFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/add"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoint"));
    }

    @Test
    public void validateTestWithValidObject() throws Exception {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();

                mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                                .param("curveId",String.valueOf(dummyCurvePoint.getCurveId()))
                                .param("term",String.valueOf(dummyCurvePoint.getTerm()))
                                .param("value",String.valueOf(dummyCurvePoint.getValue())))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/curvePoint/add"));
        Mockito.verify(curvePointService,Mockito.times(1))
                .saveCurvePoint(ArgumentMatchers.any(CurvePoint.class));
    }

    @Test
    public void validateTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate"))

                .andExpect(MockMvcResultMatchers.view().name("/curvePoint/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("must not be null")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoint"));
    }

    @Test
    public void showUpdateFormTest() throws Exception{
        CurvePoint dummyCurvePoint = getDummyCurvePoint();
        Mockito.when(curvePointService.getCurvePointById(dummyCurvePoint.getId())).thenReturn(Optional.of(dummyCurvePoint));

        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/update/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoint"));
    }

    @Test
    public void updateCurvePointTestWithValidObject() throws Exception {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();

        mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .param("curveId",String.valueOf(dummyCurvePoint.getCurveId()))
                        .param("term",String.valueOf(dummyCurvePoint.getTerm()))
                        .param("value",String.valueOf(dummyCurvePoint.getValue())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/curvePoint/list"));
        Mockito.verify(curvePointService,Mockito.times(1))
                .saveCurvePoint(ArgumentMatchers.any(CurvePoint.class));
    }

    @Test
    public void updateCurvePointTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1"))

                .andExpect(MockMvcResultMatchers.view().name("curvePoint/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("must not be null")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("curvePoint"));
    }

    @Test
    public void deleteCurvePointTest() throws Exception {
        CurvePoint dummyCurvePoint = getDummyCurvePoint();
        Mockito.when(curvePointService.getCurvePointById(dummyCurvePoint.getId())).thenReturn(Optional.of(dummyCurvePoint));

        mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/curvePoint/list"));
        Mockito.verify(curvePointService,Mockito.times(1))
                .deleteCurvePoint(ArgumentMatchers.any(CurvePoint.class));
    }

    public CurvePoint getDummyCurvePoint() {
            CurvePoint curvePoint = new CurvePoint();
            curvePoint.setId(1);
            curvePoint.setCurveId(10);
            curvePoint.setTerm(10d);
            curvePoint.setValue(30d);
            return curvePoint;
        }
}