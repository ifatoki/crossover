package com.company.resourceapi.services.impl;

import com.company.resourceapi.entities.Project;
import com.company.resourceapi.exceptions.NotFoundException;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.services.ProjectService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

	public Project getProject(long id) {
		try {
			return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
