package com.company.resourceapi;

import static com.company.resourceapi.utils.TestUtils.getProject;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import com.company.resourceapi.controllers.ProjectRestController;
import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    Project project = getProject();
 
    when(productService.getProject(ArgumentMatchers.anyLong())).thenReturn(project);

    this.mockMvc.perform(get("/api/v2/projects/5"))
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(project)));
  }

  @Test
  void whenCreateProject_thenReturn200() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Project project = getProject();

    when(productService.createProject(ArgumentMatchers.any(Project.class))).thenReturn(project);
    
    this.mockMvc.perform(MockMvcRequestBuilders.post(URI.create("/api/v2/projects"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(project)))
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(project)));
    
  }

  @Test
  void whenUpdateProject_thenReturn200() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Project project = getProject();
    Project newProject = getProject(30L, 24L);
    newProject.setName("Project Micheal");
    Project mergedProject = mergeProjects(project, newProject);

    when(productService.updateProject(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Project.class))).thenReturn(mergedProject);

    this.mockMvc.perform(MockMvcRequestBuilders.patch(URI.create("/api/v2/projects/" + project.getId()))
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(newProject)))
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(mergedProject)));
  }

  private Project mergeProjects(Project project1, Project project2) {
    if (project2.getExternalId() != null) project1.setExternalId(project2.getExternalId());
    if (project2.getSdlcSystem() != null) project1.setSdlcSystem(project2.getSdlcSystem());
    if (project2.getName() != null) project1.setName(project2.getName());
    return project1;
  }
}
