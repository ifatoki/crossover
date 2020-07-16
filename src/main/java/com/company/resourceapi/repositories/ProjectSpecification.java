package com.company.resourceapi.repositories;

import com.company.resourceapi.entities.Project;

import org.springframework.data.jpa.domain.Specification;

public class ProjectSpecification {
  
  public Specification<Project> withExternalId(String externalId) {
    return (root, query, cb) -> {
      return cb.equal(root.get("externalId"), externalId);
    };
  }

  public Specification<Project> withSdlcId(long id) {
    return (root, query, cb) -> {
      return cb.equal(root.get("sdlcSystem").get("id"), id);
    };
  }
}
