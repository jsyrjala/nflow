package com.nitorcreations.nflow.rest.v0.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nitorcreations.nflow.engine.domain.WorkflowInstance;
import com.nitorcreations.nflow.rest.v0.msg.CreateWorkflowInstanceRequest;
import com.nitorcreations.nflow.rest.v0.msg.CreateWorkflowInstanceResponse;

@Component
public class CreateWorkflowConverter {
  private final ObjectMapper objectMapper;

  @Inject
  public CreateWorkflowConverter(@Named("nflow-ObjectMapper") ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public WorkflowInstance convertAndValidate(CreateWorkflowInstanceRequest req) throws JsonProcessingException {
    WorkflowInstance.Builder builder = new WorkflowInstance.Builder().setType(req.type)
        .setBusinessKey(req.businessKey).setExternalId(req.externalId);
    if (req.activationTime == null) {
      builder.setNextActivation(DateTime.now());
    } else {
      builder.setNextActivation(req.activationTime);
    }
    if (req.requestData != null) {
      builder.setRequestData(objectMapper.writeValueAsString(req.requestData));
    }
    return builder.build();
  }

  public CreateWorkflowInstanceResponse convert(WorkflowInstance instance) {
    CreateWorkflowInstanceResponse resp = new CreateWorkflowInstanceResponse();
    resp.id = instance.id;
    resp.type = instance.type;
    resp.businessKey = instance.businessKey;
    resp.externalId = instance.externalId;
    return resp;
  }

}
