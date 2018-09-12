package com.github.fabiojose.ownership.flow;

import com.github.fabiojose.ownership.state.OwnershipState;
import com.github.fabiojose.ownership.contract.OwnershipContract;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;

import net.corda.core.identity.Party;

import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.flows.FinalityFlow;

import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.ProgressTracker.Step;

import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.contracts.Command;

import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;

import java.util.UUID;

@InitiatingFlow
@StartableByRPC
public final class TransferFlow extends FlowLogic<SignedTransaction>{

    private final Step GENERATING_TRANSACTION = new Step("Generating transaction to perform the transfer to new owner");
    private final Step VERIFYING_TRANSACTION  = new Step("Verifying contract constraints");
    private final Step SIGNING_TRANSACTION    = new Step("Signing transaction with our private key");
    private final Step FINALIZING_TRANSACTION = new Step("Finalizing proposed transaction");

    private final ProgressTracker tracker = new ProgressTracker(
      GENERATING_TRANSACTION,
      VERIFYING_TRANSACTION,
      SIGNING_TRANSACTION,
      FINALIZING_TRANSACTION    
    );

    private final String ownershipID;
    private final Party newOwner;
    private final Party bank;

    public TransferFlow(final String ownershipID, final Party newOwner, final Party bank){
      this.ownershipID = ownershipID;
      this.newOwner    = newOwner;
      this.bank        = bank;
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

      // 1.
      tracker.setCurrentStep(GENERATING_TRANSACTION);
      
      // Get the reference to my identity
      final Party me = getServiceHub().getMyInfo().getLegalIdentities().get(0);

      // Get the reference to current state, where I am the owner
      //final QueryCriteria generalCriteria = new VaultQueryCriteria(Vault.StateStatus.ALL);
      final QueryCriteria criteria = new QueryCriteria.LinearStateQueryCriteria(ImmutableList.of(me), ImmutableList.of(UUID.fromString(ownershipID)));
      final Vault.Page<OwnershipState> results = getServiceHub().getVaultService().queryBy(OwnershipState.class, criteria);

      getLogger().info(" >>>>>>>>>>>>> " + results.toString());

      // Instance the state
      //final OwnershipState state = new OwnershipState(object, description, value, currency, me, new UniqueIdentifier());

      // Instance the command used in the transaction
      //final Command<OwnershipContract.Commands.Issue> issue = new Command<>(new OwnershipContract.Commands.Issue(), ImmutableList.of(me.getOwningKey()));

      // Create a transaction builder instance to issue the ownership for me
      /*
      final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                                                 .addOutputState(state, OwnershipContract.OWNERSHIP_CONTRACT_ID)
                                                 .addCommand(issue);
      
      // 2.
      tracker.setCurrentStep(VERIFYING_TRANSACTION);

      // Execute the contract's logic
      txBuilder.verify(getServiceHub());
    
      // 3.
      tracker.setCurrentStep(SIGNING_TRANSACTION);
      final SignedTransaction partSignedTx = getServiceHub().signInitialTransaction(txBuilder);

      // 4.
      tracker.setCurrentStep(FINALIZING_TRANSACTION);
      return subFlow(new FinalityFlow(partSignedTx));
      */
      return null;
    }
}
