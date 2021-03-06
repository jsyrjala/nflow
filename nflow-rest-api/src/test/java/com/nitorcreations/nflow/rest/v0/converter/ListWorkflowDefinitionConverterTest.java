package com.nitorcreations.nflow.rest.v0.converter;

import static com.nitorcreations.Matchers.reflectEquals;
import static com.nitorcreations.nflow.rest.v0.DummyTestWorkflow.State.end;
import static com.nitorcreations.nflow.rest.v0.DummyTestWorkflow.State.error;
import static com.nitorcreations.nflow.rest.v0.DummyTestWorkflow.State.start;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.nitorcreations.nflow.rest.v0.DummyTestWorkflow;
import com.nitorcreations.nflow.rest.v0.msg.ListWorkflowDefinitionResponse;
import com.nitorcreations.nflow.rest.v0.msg.State;

@RunWith(MockitoJUnitRunner.class)
public class ListWorkflowDefinitionConverterTest {

  private ListWorkflowDefinitionConverter converter;

  @Before
  public void setup() {
    converter = new ListWorkflowDefinitionConverter();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void convertWorks() {
    DummyTestWorkflow def = new DummyTestWorkflow();
    ListWorkflowDefinitionResponse resp = converter.convert(def);
    assertThat(resp.type, is(def.getType()));
    assertThat(resp.name, is(def.getName()));
    assertThat(resp.description, is(def.getDescription()));
    assertThat(resp.onError, is(def.getErrorState().name()));
    assertThat(resp.states, arrayContainingInAnyOrder(
        reflectEquals(getResponseState(end, Collections.<String>emptyList(), null)),
        reflectEquals(getResponseState(error, asList(end.name()), null)),
        reflectEquals(getResponseState(start, asList(end.name(), error.name()), error.name()))));
    assertThat((int)resp.settings.transitionDelaysInMilliseconds.immediate, is(def.getSettings().getImmediateTransitionDelay()));
    assertThat((int)resp.settings.transitionDelaysInMilliseconds.waitShort, is(def.getSettings().getShortTransitionDelay()));
    assertThat((int)resp.settings.transitionDelaysInMilliseconds.waitError, is(def.getSettings().getErrorTransitionDelay()));
    assertThat(resp.settings.maxRetries, is(def.getSettings().getMaxRetries()));
  }

  private State getResponseState(DummyTestWorkflow.State workflowState,
      List<String> nextStateNames, String errorStateName) {
    State state = new State(workflowState.name(),
        workflowState.getType().name(), workflowState.getName(), workflowState.getDescription());
    state.transitions.addAll(nextStateNames);
    state.onFailure = errorStateName;
    return state;
  }

}
