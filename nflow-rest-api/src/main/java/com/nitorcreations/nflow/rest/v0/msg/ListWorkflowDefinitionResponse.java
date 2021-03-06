package com.nitorcreations.nflow.rest.v0.msg;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@ApiModel(value = "Basic information of workflow definition")
@SuppressFBWarnings(value="URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD", justification="jackson reads dto fields")
public class ListWorkflowDefinitionResponse {

  @ApiModelProperty(value = "Type of the workflow definition", required=true)
  public String type;

  @ApiModelProperty(value = "Name of the workflow definition", required=true)
  public String name;

  @ApiModelProperty(value = "Description workflow definition", required=false)
  public String description;

  @ApiModelProperty(value = "Version of the workflow definition", required=true)
  public int version;

  @ApiModelProperty(value = "Generic error state", required=true)
  public String onError;

  @ApiModelProperty(value = "Workflow definition states and transitions", required=true)
  public State[] states;

  @ApiModelProperty(value = "Workflow settings", required=true)
  public Settings settings;


  public static class Settings {

    @ApiModelProperty(value = "Global transition delays for the workflow", required=true)
    public TransitionDelays transitionDelaysInMilliseconds;

    @ApiModelProperty(value = "Maximum retries for a state before moving to failure", required=true)
    public int maxRetries;

  }

  public static class TransitionDelays {

    @ApiModelProperty(value = "Delay in immediate transition", required=true)
    public long immediate;

    @ApiModelProperty(value = "Short delay between transitions", required=true)
    public long waitShort;

    @ApiModelProperty(value = "Maximum retries for a state before moving to failure", required=true)
    public long waitError;

  }

}
