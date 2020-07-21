package com.company.resourceapi;

import static com.company.resourceapi.utils.TestUtils.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import com.company.resourceapi.entities.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
public class ProjectRestIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void whenDefaultRoute_thenReturn404() throws Exception {
    this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void whenGetProjects_thenReturn200() throws Exception {
    // Given the data loaded from data.sql

    // Then
    this.mockMvc.perform(get("/api/v2/projects/" + 1L))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.name", is("Sample Project")));
  }

  @Test
  void whenGetNotFoundProjectIndex_thenReturn404() throws Exception {
    // Given the data loaded from data.sql

    // Then
    this.mockMvc.perform(get("/api/v2/projects/" + 1000L))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message", is("Project with Id 1000 not found")));
  }

  @Test
  void whenGetInvalidProjectIndex_thenReturn400() throws Exception {
    // Given the data loaded from data.sql

    // Then
    this.mockMvc.perform(get("/api/v2/projects/itunuloluwa"))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message", is("invalid project Id")));
  }

  @Test
  void whenPostValidProjects_thenReturn200() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    // Given the data loaded from data.sql

    // When
    Project project = getProject("NEVER_EVER");

    // Then
    this.mockMvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v2/projects"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(project)))
      .andExpect(status().isCreated())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.sdlcSystem.id", is((int)project.getSdlcSystem().getId())))
      .andExpect(jsonPath("$.externalId", is(project.getExternalId())));
  }

  @Test
  void whenPostConflictingProject_thenReturn409() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    
    // Given the data loaded from data.sql and the declarations above,

    // When
    Project project = getProject("PROJECTX");

    // Then
    this.mockMvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v2/projects"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(project)))
      .andExpect(status().isConflict())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message", is("Project with externalId: " + project.getExternalId() + " and sdlcSystem with Id: " + project.getSdlcSystem().getId() + " already exists")));
  }

  @Test
  void whenPostProjectWithNonExistingSdlcSystem_thenReturn404() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();

    // Given the data loaded from data.sql and the declarations above,
    Project project = getProject(1L, 12L);

    // Then
    this.mockMvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v2/projects"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(project)))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message", is("Sdlc System with Id " + project.getSdlcSystem().getId() + " not found")));
  }
}