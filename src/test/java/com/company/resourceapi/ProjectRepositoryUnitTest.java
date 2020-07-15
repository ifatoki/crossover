package com.company.resourceapi;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.SdlcSystemRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class ProjectRepositoryUnitTest {
  
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private SdlcSystemRepository sdlcSystemRepository;

  private Date testTime = new Date();

  @Test
  public void whenFindById_thenReturnProject() throws Exception {
    // Given the loaded data from the default schema.sql and data.sql
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(1L).get();
    Project newProject = new Project(133L, "TEST_ID", "new test project", testSdlcSystem, testTime.toInstant(), testTime.toInstant());
    newProject = entityManager.merge(newProject);
    entityManager.flush();

    // When 
    Project project = projectRepository.findById(newProject.getId()).get();
    
    // Then
    assertThat(project.getName())
      .as("Name should be equal to ")
      .isEqualTo("new test project");
    assertThat(project.getExternalId())
      .as("ExternalId should be equal to ")
      .isEqualTo("TEST_ID");
    assertThat(project.getSdlcSystem())
      .as("SdlcSystem should be equal to ")
      .usingRecursiveComparison()
      .isEqualTo(testSdlcSystem);
  }

  @Test
  public void whenFindAll_thenReturnAllProjects() throws Exception {
    // Given the loaded data from the default schema.sql and data.sql
    long initialSize = projectRepository.findAll().size();
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(1L).get();
    Project newProject = new Project(133L, "TEST_ID", "new test project", testSdlcSystem, testTime.toInstant(), testTime.toInstant());
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
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(1L).get();
    Project newProject = new Project(133L, "TEST_ID", "new test project", testSdlcSystem, testTime.toInstant(), testTime.toInstant());

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
    // Given the loaded data from the default schema.sql and data.sql
    SdlcSystem testSdlcSystem = sdlcSystemRepository.findById(1L).get();
    Project newProject = new Project(133L, "TEST_ID", "new test project", testSdlcSystem, testTime.toInstant(), testTime.toInstant());
    newProject = entityManager.merge(newProject);
    entityManager.flush();

    // When
    Project project = projectRepository.findBySdlcSystemIdAndId(testSdlcSystem.getId(), newProject.getId()).get();

    // Then
    assertThat(project)
      .usingRecursiveComparison()
      .isEqualTo(newProject);
  }
}