package com.github.fabiojose.ownership.flow;

import com.github.fabiojose.ownership.state.OwnershipState;

import co.paralleluniverse.fibers.Suspendable;

import net.corda.core.identity.Party;

import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowException;

import net.corda.core.transactions.SignedTransaction;

import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import net.corda.core.contracts.UniqueIdentifier;

public final class IssueFlow {

  @InitiatingFlow
  public static class Initiator extends FlowLogic<SignedTransaction> {

    private final Step GENERATING_TRANSACTION = new Step("Generating transaction on new ownership");
    private final Step VERIFYING_TRANSACTION  = new Step("Verifying contract constraints");
    private final Step SIGNING_TRANSACTION    = new Step("Signing transaction with our private key");
    private final Step FINALIZING_TRANSACTION = new Step("Finalizing proposed transaction");

    private final ProgressTracker tracker = new ProgressTracker(
      GENERATING_TRANSACTION,
      VERIFYING_TRANSACTION,
      SIGNING_TRANSACTION,
      FINALIZING_TRANSACTION    
    );

    private final String object;
    private final String description;
    private final Double value;
    private final String currency;
    private final Party owner;

    public Initiator(final String object, final String description, final Double value, final String currency, final Party owner){
      this.object = object;
      this.description = description;
      this.value = value;
      this.currency = currency;
      this.owner = owner;
    }

    @Override
    public ProgressTracker getProgressTracker(){
      return tracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

      // Get the reference to notary's identity
      final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

      // *begin* 1.
      tracker.setCurrentStep(GENERATING_TRANSACTION);
      
      // Get the reference to my identity
      final Party me = getServiceHub().getMyInfo().getLegalIdentities().get(0);

      // Instance the state
      final OwnershipState state = new OwnershipState(object, description, value, currency, me, new UniqueIdentifier());
      return null;
    }
  }
}
