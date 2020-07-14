package com.company.resourceapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.resourceapi.services.ProjectService;
import com.company.resourceapi.services.impl.ProjectServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;


@SpringBootTest
@AutoConfigureMockMvc
public class ProjectRestIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void whenDefaultRoute_thenReturn404() throws Exception {
    this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
  }
}