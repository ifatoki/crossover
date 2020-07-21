package com.company.resourceapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.exceptions.ConflictException;
import com.company.resourceapi.exceptions.InvalidRequestBodyException;
import com.company.resourceapi.exceptions.NotFoundException;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.ProjectSpecification;
import com.company.resourceapi.repositories.SdlcSystemRepository;
import com.company.resourceapi.services.ProjectService;
import com.company.resourceapi.services.impl.ProjectServiceImpl;

import org.hibernate.cfg.VerifyFetchProfileReferenceSecondPass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceUnitTest {
  @Autowired
  ProjectService projectService;

  private Date testTime = new Date();
  private List<Project> emptyProjects = new ArrayList<Project>();
  private List<Project> projects = new ArrayList<Project>();
  private SdlcSystem testSdlcSystem;
  private Project newProject;

  @Mock ProjectRepository projectRepository;
  @Mock SdlcSystemRepository sdlcSystemRepository;
  @Mock ProjectSpecification projectSpecification;

  @BeforeEach
  void setup() {
    projectService = new ProjectServiceImpl(projectRepository, sdlcSystemRepository, projectSpecification);
    testSdlcSystem = new SdlcSystem(65L, "https://java.itunufatoki.com", "A test description", testTime.toInstant(), testTime.toInstant());
    newProject = new Project(133L, "TEST_ID", "new test project", testSdlcSystem, testTime.toInstant(), testTime.toInstant());
    projects.add(newProject);

    lenient().when(projectRepository.findById(newProject.getId())).thenReturn(Optional.of(newProject));
    lenient().when(sdlcSystemRepository.findById(testSdlcSystem.getId())).thenReturn(Optional.of(testSdlcSystem));
    lenient().when(projectRepository.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(emptyProjects);
    lenient().when(projectRepository.save(ArgumentMatchers.any(Project.class))).thenReturn(newProject);
  }

  @Test
  void whenGetProject_thenReturnMatchingProject() throws Exception {
    // Given the setup above;

    // When 
    Project project = projectService.getProject(133L);

    // Then
    verify(projectRepository).findById(133L);
    assertThat(project.getExternalId()).isEqualTo("TEST_ID");
    assertThat(project.getName()).isEqualTo("new test project");
    assertThat(project.getSdlcSystem().getId()).isEqualTo(65L);
  }

  @Test
  void whenGetProjectWithInvalidId_thenThrowNotFoundException() throws Exception {
    assertThrows(NotFoundException.class, () -> {
      projectService.getProject(4438L);
    });
    verify(projectRepository).findById(4438L);
  }

  @Test
  void whenCreateProject_thenCreateAndReturnProject() throws Exception {
    // Given the following;
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(65L).get();
    Project newProject = new Project(13L, "TEST_ID_2", "new test project 2", testSdlcSystem, testTime.toInstant(), testTime.toInstant());

    // When
    projectService.createProject(newProject);

    // Then
    verify(projectRepository).save(newProject);
    verify(sdlcSystemRepository, times(3)).findById(testSdlcSystem.getId());
    verify(projectRepository).findAll(ArgumentMatchers.any(Specification.class));
  }

  @Test
  void whenCreateConflictingProject_thenThrowConflictException() throws Exception {
    // Given the following
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(65L).get();
    Project newProject = new Project(13L, "TEST_ID_2", "new test project 2", testSdlcSystem, testTime.toInstant(), testTime.toInstant());
    lenient().when(projectRepository.findAll(ArgumentMatchers.any(Specification.class))).thenReturn(projects);

    // Then
    assertThrows(ConflictException.class, () -> {
      projectService.createProject(newProject);
    });
    verify(sdlcSystemRepository, times(3)).findById(testSdlcSystem.getId());
  }

  @Test
  void whenUpdateProject_thenThrowNotFoundForInvalidProjectId() throws Exception {
    // Given the conditions above

    // Then
    assertThrows(NotFoundException.class, () -> {
      projectService.updateProject(500L, newProject);
    });
    verify(projectRepository).findById(500L);
  }

  void whenUpdateProject_thenCallSaveProject() throws Exception {
    // Given the following;
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(65L).get();
    Project newProject = new Project(13L, "TEST_ID_2", "new test project 2", testSdlcSystem, testTime.toInstant(), testTime.toInstant());

    // When
    projectService.updateProject(133L, newProject);

    // Then
    verify(projectRepository).save(newProject);
    verify(sdlcSystemRepository, times(2)).findById(testSdlcSystem.getId());
    verify(projectRepository).findAll(ArgumentMatchers.any(Specification.class));
  }
}
