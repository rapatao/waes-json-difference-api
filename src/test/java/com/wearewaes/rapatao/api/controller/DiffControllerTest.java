package com.wearewaes.rapatao.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wearewaes.rapatao.api.domain.RequestData;
import com.wearewaes.rapatao.api.domain.ResponseType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
public class DiffControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;


    @Test
    public void shouldAddBothSidesAndGetEquals() throws Exception {
        final String diffId = UUID.randomUUID().toString();

        final String jsonAsString = objectMapper.writeValueAsString(RequestData.builder()
                .value("d2UgYXJlIHdhZXM=")
                .build());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/left", diffId)
                .content(jsonAsString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/right", diffId)
                .content(jsonAsString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(ResponseType.EQUALS.toString())));
    }

    @Test
    public void shouldAddBothSidesWithDifferentSizesAndGetTheResultAsDifferentSize() throws Exception {
        final String diffId = UUID.randomUUID().toString();

        final String leftSideContent = objectMapper.writeValueAsString(RequestData.builder()
                .value("d2UgYXJlIHdhZXM=")
                .build());

        final String rightSideContent = objectMapper.writeValueAsString(RequestData.builder()
                .value("eWVzLCB3ZSBhcmUgd2Flcw==")
                .build());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/left", diffId)
                .content(leftSideContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/right", diffId)
                .content(rightSideContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(ResponseType.DIFFERENT_SIZE.toString())));
    }

    @Test
    public void shouldAddBothSidesWithSameSizeAndGetTheResultAsDifferentContent() throws Exception {
        final String diffId = UUID.randomUUID().toString();

        final String leftSideContent = objectMapper.writeValueAsString(RequestData.builder()
                .value("eWVzLCB3ZSBhcmUgd2Flcw==")
                .build());

        final String rightSideContent = objectMapper.writeValueAsString(RequestData.builder()
                .value("bm93LCB3ZSBhcmUgd2Flcw==")
                .build());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/left", diffId)
                .content(leftSideContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/right", diffId)
                .content(rightSideContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(ResponseType.DIFFERENT_CONTENT.toString())))
                .andExpect(content().string(containsString("Offsets: 0,1,2,3 - Length: 24")));
    }

    @Test
    public void shouldAddOneSideAndGotErrorWhenGetDiff() throws Exception {
        final String diffId = UUID.randomUUID().toString();

        final String leftSideContent = objectMapper.writeValueAsString(RequestData.builder()
                .value("d2UgYXJlIHdhZXM=")
                .build());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/left", diffId)
                .content(leftSideContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldGetNotFoundWhenGetDiffFromInvalidIdentifier() throws Exception {
        final String diffId = UUID.randomUUID().toString();

        mvc.perform(MockMvcRequestBuilders
                .get("/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRejectBothSidesWithEmptyData() throws Exception {
        final String diffId = UUID.randomUUID().toString();

        final String jsonAsString = objectMapper.writeValueAsString(RequestData.builder().build());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/left", diffId)
                .content(jsonAsString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders
                .get("/dif/{id}", diffId))
                .andExpect(status().isNotFound());

        mvc.perform(MockMvcRequestBuilders
                .put("/diff/{id}/right", diffId)
                .content(jsonAsString)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(MockMvcRequestBuilders
                .get("/dif/{id}", diffId))
                .andExpect(status().isNotFound());
    }

}