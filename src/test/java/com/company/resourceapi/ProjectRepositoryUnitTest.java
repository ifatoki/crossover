package com.company.resourceapi;

import static com.company.resourceapi.utils.TestUtils.appendDates;
import static com.company.resourceapi.utils.TestUtils.getProject;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.SdlcSystemRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ProjectRepositoryUnitTest {
  
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private SdlcSystemRepository sdlcSystemRepository;

  @Test
  public void whenFindById_thenReturnProject() throws Exception {
    // Given the loaded data from the default schema.sql and data.sql
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(1L).get();
    Project newProject = (Project)appendDates(getProject());
    newProject.setSdlcSystem(testSdlcSystem);
    newProject = entityManager.merge(newProject);
    entityManager.flush();

    // When 
    Project project = projectRepository.findById(newProject.getId()).get();
    
    // Then
    assertThat(project.getName())
      .as("Name should be equal to ")
      .isEqualTo(newProject.getName());
    assertThat(project.getExternalId())
      .as("ExternalId should be equal to ")
      .isEqualTo(newProject.getExternalId());
    assertThat(project.getSdlcSystem())
      .as("SdlcSystem should be equal to ")
      .usingRecursiveComparison()
      .isEqualTo(testSdlcSystem);
  }

  @Test
  public void whenFindAll_thenReturnAllProjects() throws Exception {
    // Given the loaded data from the default schema.sql and data.sql
    long initialSize = projectRepository.findAll().size();
    Project newProject = (Project)appendDates(getProject());
    newProject = entityManager.merge(newProject);
    entityManager.flush();

    // When
    List<Project> projects = projectRepository.findAll();

    // Then
    assertThat(initialSize).isEqualTo(8L);
    assertThat(projects.size()).isEqualTo(initialSize + 1);
    assertThat(projects.get(1).getClass().getSimpleName()).isEqualTo(Project.class.getSimpleName());
  }

  @Test
  public void whenSave_thenCommitProjectToMemory() throws Exception {
    // Given the loaded data from the default schema.sql and data.sql
    long initialSize = projectRepository.findAll().size();
    Project newProject = (Project)appendDates(getProject());

    // When
    Project project = projectRepository.save(newProject);
    List<Project> projects = projectRepository.findAll();

    // Then
    assertThat(projects.size()).isEqualTo(initialSize + 1);
    assertThat(projects.get(projects.size() - 1))
      .usingRecursiveComparison()
      .isEqualTo(project);
  }

  @Test
  public void whenfindBySdlcSystemIdAndId_thenReturnMatchingProject() throws Exception {
    Project newProject = (Project)appendDates(getProject());
    newProject = entityManager.merge(newProject);
    entityManager.flush();

    // When
    Project project = projectRepository.findBySdlcSystemIdAndId(newProject.getSdlcSystem().getId(), newProject.getId()).get();

    // Then
    assertThat(project)
      .usingRecursiveComparison()
      .isEqualTo(newProject);
  }
}