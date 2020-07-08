package com.company.resourceapi.services.impl;

import java.util.Optional;
// import java.sql.SQLIntegrityConstraintViolationException;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.entities.SdlcSystem;
import com.company.resourceapi.exceptions.ConflictException;
import com.company.resourceapi.exceptions.InvalidRequestBodyException;
import com.company.resourceapi.exceptions.NotFoundException;
import com.company.resourceapi.exceptions.NotFoundSdlcSystemException;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.SdlcSystemRepository;
import com.company.resourceapi.services.ProjectService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private final ProjectRepository projectRepository;

	@Autowired
	private final SdlcSystemRepository sdlcSystemRepository;

	public Project getProject(long id) {
		return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
	}

	public Project createProject(Project projectDetails) {
		if (projectDetails.getExternalId() == null)
			throw new InvalidRequestBodyException("externalId");
		if (projectDetails.getSdlcSystem() == null)
			throw new InvalidRequestBodyException("sdlcSystem");
		Project project = setSdlcSystem(projectDetails, projectDetails.getSdlcSystem().getId());
		return saveProject(project);
	}

	public Project updateProject(long id, Project projectDetails) {
		SdlcSystem sdlcSystem = projectDetails.getSdlcSystem();
		String externalId = projectDetails.getExternalId();
		String name = projectDetails.getName();

		System.out.println(projectDetails);
		return projectRepository.findById(id)
			.map(project -> {
				if (externalId != null) project.setExternalId(externalId);
				if (name != null) project.setName(name);
				if (sdlcSystem != null) {
					project = setSdlcSystem(project, sdlcSystem.getId());
				}
				return saveProject(project);
			}).orElseThrow(() -> new NotFoundException(Project.class, id));
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

	private Project setSdlcSystem(Project project, long sdlcSystemId) {
		return sdlcSystemRepository.findById(sdlcSystemId)
			.map(sdlcSystem -> {
				project.setSdlcSystem(sdlcSystem);
				return project;
			}).orElseThrow(() -> new NotFoundSdlcSystemException(SdlcSystem.class, sdlcSystemId));
	}
}
