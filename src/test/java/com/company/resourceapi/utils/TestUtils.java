package com.company.resourceapi.utils;

import java.util.Date;
import java.util.Locale;

import javax.persistence.Entity;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.github.javafaker.Faker;

public class TestUtils {

  private static long DEFAULT_PROJECT_ID = 24L;
  private static long DEFAULT_SDLC_SYSTEM_ID = 1L;
  private static Date testTime = new Date();
  private static Faker faker = new Faker(new Locale("en-NG"));

  public static Project getProject(String externalId) {
    Project project = getProject();
    project.setExternalId(externalId);
    return project;
  }

  public static Project getProject() {
    return getProject(DEFAULT_PROJECT_ID, DEFAULT_SDLC_SYSTEM_ID);
  }

  public static Project getProject(long projectId, long sdlcSystemId) {
    SdlcSystem sdlcSystem = getSdlcSystem(sdlcSystemId);

    Project project = new Project();
    project.setId(projectId);
    project.setSdlcSystem(sdlcSystem);
    project.setExternalId(faker.name().firstName());

    return project;
  }

  public static SdlcSystem getSdlcSystem(long sdlcSystemId) {
    SdlcSystem sdlcSystem = new SdlcSystem();
    sdlcSystem.setId(sdlcSystemId);
    sdlcSystem.setBaseUrl(faker.internet().url());
    sdlcSystem.setDescription(faker.lorem().sentence(4));
    return sdlcSystem;
  }

  public static SdlcSystem getSdlcSystem() {
    return getSdlcSystem(DEFAULT_SDLC_SYSTEM_ID);
  }

  public static SdlcSystem getSdlcSystem(String url) {
    SdlcSystem sdlcSystem = new SdlcSystem();
    sdlcSystem.setBaseUrl(url);
    sdlcSystem.setDescription(faker.lorem().sentence(4));
    return sdlcSystem;
  }

  public static Object appendDates(Object obj){
    Project project;
    SdlcSystem sdlcSystem;

    if (obj.getClass() == Project.class) {
      project = (Project)obj;
      project.setLastModifiedDate(testTime.toInstant());
      project.setCreatedDate(testTime.toInstant());
      return project;
    } else if (obj.getClass() == SdlcSystem.class) {
      sdlcSystem = (SdlcSystem)obj;
      sdlcSystem.setLastModifiedDate(testTime.toInstant());
      sdlcSystem.setCreatedDate(testTime.toInstant());
      return sdlcSystem;
    } else {
      throw new ClassCastException("You can only append dates to objects in the Project and SdlcSystem class");
    }
  }
}