package com.company.resourceapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.company.resourceapi.controllers.ProjectRestController;
import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.cfg.SetSimpleValueTypeSecondPass;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ProjectRestController.class)
public class ProjectRestControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProjectService productService;

  @Test
  void whenDefaultRoute_thenReturn404() throws Exception {
    this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void whenGetProject_thenReturn200() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    SdlcSystem sdlcSystem = new SdlcSystem();
    sdlcSystem.setId(1L);
    Project project = new Project();
    project.setId(24L);
    project.setSdlcSystem(sdlcSystem);
    project.setExternalId("NEVER_EVER");
 
    when(productService.getProject(ArgumentMatchers.anyLong())).thenReturn(project);

    this.mockMvc.perform(get("/api/v2/projects/5"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(project)));
  }

  @Test
  void whenCreateProject_thenReturn200() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    SdlcSystem sdlcSystem = new SdlcSystem();
    sdlcSystem.setId(1L);
    Project project = new Project();
    project.setId(24L);
    project.setSdlcSystem(sdlcSystem);
    project.setExternalId("NEVER_EVER");

    when(productService.createProject(ArgumentMatchers.any(Project.class))).thenReturn(project);
    
    this.mockMvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v2/projects"))
      .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
      .content(objectMapper.writeValueAsString(project)))
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(project)));
    
  }

  // @Test
  // void when () throws Exception {
  //   this.mockMvc.
  // }


}