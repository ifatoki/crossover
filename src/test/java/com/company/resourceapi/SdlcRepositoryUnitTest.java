package com.company.resourceapi;

import static com.company.resourceapi.utils.TestUtils.appendDates;
import static com.company.resourceapi.utils.TestUtils.getSdlcSystem;
import static org.assertj.core.api.Assertions.assertThat;

import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.repositories.SdlcSystemRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class SdlcRepositoryUnitTest {
  
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SdlcSystemRepository sdlcSystemRepository;

  @Test
  public void whenFindById_thenReturnSdlcSystem() throws Exception {
    // Given the loaded Data from the default schema.sql and data.sql
    SdlcSystem newSdlcSystem = (SdlcSystem)appendDates(getSdlcSystem("https://java.itunufatoki.com"));
    newSdlcSystem = entityManager.merge(newSdlcSystem);
    entityManager.flush();

    // When
    SdlcSystem sdlcSystem = sdlcSystemRepository.findById(newSdlcSystem.getId()).get();

    // Then
    assertThat(sdlcSystem.getId()).isEqualTo(newSdlcSystem.getId());
    assertThat(sdlcSystem.getBaseUrl()).isEqualTo(newSdlcSystem.getBaseUrl());
    assertThat(sdlcSystem.getDescription()).isEqualTo(newSdlcSystem.getDescription());
  }
}
