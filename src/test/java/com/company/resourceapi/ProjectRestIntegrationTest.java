package com.company.resourceapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


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