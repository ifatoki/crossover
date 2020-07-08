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
		long id = projectDetails.getSdlcSystem().getId();
		return sdlcSystemRepository.findById(id)
			.map(sdlcSystem -> {
				projectDetails.setSdlcSystem(sdlcSystem);
				try {
					return projectRepository.save(projectDetails);
				} catch (Exception e) {
					if (e instanceof DataIntegrityViolationException) {
						throw new ConflictException(id, projectDetails.getExternalId());
					} 
					throw new ServerErrorException(e.getMessage(), e);
				}
			}).orElseThrow(() -> new NotFoundSdlcSystemException(SdlcSystem.class, id));
	}
}
