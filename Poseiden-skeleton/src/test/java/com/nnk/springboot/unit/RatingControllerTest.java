package com.nnk.springboot.unit;

import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
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
@WebMvcTest(controllers = RatingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RatingControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RatingService ratingService;


    @Test
    @WithMockUser
    public void homeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/list"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ratings"));
    }

    @Test
    public void addRatingFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/add"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rating"));
    }

    @Test
    public void validateTestWithValidObject() throws Exception {
        Rating dummyRating = getDummyRating();

        mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
                        .param("moodysRating",dummyRating.getMoodysRating())
                        .param("sandPRating",dummyRating.getSandPRating())
                        .param("fitchRating",dummyRating.getFitchRating())
                        .param("orderNumber",String.valueOf(dummyRating.getOrderNumber())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/rating/add"));
       Mockito.verify(ratingService,Mockito.times(1))
                .saveRating(ArgumentMatchers.any(Rating.class));
    }

    @Test
    public void validateTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate"))

                .andExpect(MockMvcResultMatchers.view().name("/rating/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("rating is mandatory")))
                        .andExpect(MockMvcResultMatchers.model().attributeExists("rating"));
    }

    @Test
    public void showUpdateFormTest() throws Exception{
        Rating dummyRating = getDummyRating();
        Mockito.when(ratingService.getRatingById(dummyRating.getId())).thenReturn(Optional.of(dummyRating));

        mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/update"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rating"));
    }

    @Test
    public void updateRatingTestWithValidObject() throws Exception {
        Rating dummyRating = getDummyRating();

        mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .param("moodysRating",dummyRating.getMoodysRating())
                        .param("sandPRating",dummyRating.getSandPRating())
                        .param("fitchRating",dummyRating.getFitchRating())
                        .param("orderNumber",String.valueOf(dummyRating.getOrderNumber())))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/rating/list"));
        Mockito.verify(ratingService,Mockito.times(1))
                .saveRating(ArgumentMatchers.any(Rating.class));
    }

    @Test
    public void updateRatingTestWithWrongObject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1"))

                .andExpect(MockMvcResultMatchers.view().name("rating/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("rating is mandatory")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("rating"));
    }

    @Test
    public void deleteRatingTest() throws Exception {
        Rating dummyRating = getDummyRating();
        Mockito.when(ratingService.getRatingById(dummyRating.getId())).thenReturn(Optional.of(dummyRating));

        mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/rating/list"));
        Mockito.verify(ratingService,Mockito.times(1))
                .deleteRating(ArgumentMatchers.any(Rating.class));
    }


    public Rating getDummyRating() {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("Moodys Rating");
        rating.setSandPRating("Sand PRating");
        rating.setFitchRating("Fitch Rating");
        rating.setOrderNumber(10);
        return rating;
    }
}
