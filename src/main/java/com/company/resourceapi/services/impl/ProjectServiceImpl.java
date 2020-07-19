package com.company.resourceapi.services.impl;

import java.util.List;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.exceptions.ConflictException;
import com.company.resourceapi.exceptions.InvalidRequestBodyException;
import com.company.resourceapi.exceptions.NotFoundException;
import com.company.resourceapi.exceptions.NotFoundSdlcSystemException;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.ProjectSpecification;
import com.company.resourceapi.repositories.SdlcSystemRepository;
import com.company.resourceapi.services.ProjectService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private SdlcSystemRepository sdlcSystemRepository;

	@Autowired
	private ProjectSpecification projectSpecification;

	public Project getProject(long id) {
		return projectRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(Project.class, id));
	}

	public Project createProject(Project projectDetails) {
		if (projectDetails.getExternalId() == null)
			throw new InvalidRequestBodyException("externalId");
		if (projectDetails.getSdlcSystem() == null)
			throw new InvalidRequestBodyException("sdlcSystem");
		projectDetails = verifyKeyConstraints(projectDetails);
		return saveProject(projectDetails);
	}

	public Project updateProject(long id, Project projectDetails) {
		SdlcSystem sdlcSystem = projectDetails.getSdlcSystem();
		String externalId = projectDetails.getExternalId();
		String name = projectDetails.getName();

		return projectRepository.findById(id)
			.map(project -> {
				if (externalId != null) project.setExternalId(externalId);
				if (name != "") project.setName(name);
				if (sdlcSystem != null) {
					project = setSdlcSystem(project, sdlcSystem.getId());
				}
				project = verifyKeyConstraints(project);
				return saveProject(project);
			})
			.orElseThrow(() -> new NotFoundException(Project.class, id));
	}

	private Project saveProject(Project project) {
		try {
			return projectRepository.save(project);
		} catch (Exception e) {
			if (e instanceof DataIntegrityViolationException) {
				throw new ConflictException(project.getSdlcSystem().getId(), project.getExternalId());
			} 
			throw new ServerErrorException(e.getMessage(), e);
		}
	}

	private Project verifyKeyConstraints(Project project) {
		long sdlcSystemId = project.getSdlcSystem().getId();

		return sdlcSystemRepository.findById(sdlcSystemId)
			.map(sdlcSystem -> {
				List<Project> duplicateProjects = projectRepository.findAll(
					Specification.where(
						projectSpecification.withExternalId(project.getExternalId())
					).and(
						projectSpecification.withSdlcId(sdlcSystemId)
					)
				);
				if (!duplicateProjects.isEmpty() && project.getId() != duplicateProjects.get(0).getId()) 
					throw new ConflictException(sdlcSystemId, project.getExternalId());
				return project;
			})
			.orElseThrow(() -> new NotFoundSdlcSystemException(SdlcSystem.class, sdlcSystemId));
	}

	private Project setSdlcSystem(Project project, long sdlcSystemId) {
		return sdlcSystemRepository.findById(sdlcSystemId)
			.map(sdlcSystem -> {
				project.setSdlcSystem(sdlcSystem);
				return project;
			})
			.orElseThrow(() -> new NotFoundSdlcSystemException(SdlcSystem.class, sdlcSystemId));
	}
}
