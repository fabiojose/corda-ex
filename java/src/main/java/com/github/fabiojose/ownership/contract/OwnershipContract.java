package com.github.fabiojose.ownership.contract;

import com.github.fabiojose.ownership.state.OwnershipState;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.core.identity.AbstractParty;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

import java.util.stream.Collectors;
import java.util.List;

import org.slf4j.LoggerFactory;

public class OwnershipContract implements Contract {
  public static final String OWNERSHIP_CONTRACT_ID = "com.github.fabiojose.ownership.contract.OwnershipContract";

  private static final int FIRST = 0;

  @Override
  public void verify(LedgerTransaction tx) {
    final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

    LoggerFactory.getLogger(OwnershipContract.class).info(" > > > > CMD" + command);

    // Issing (emiting) the ownership record
    if(command.getValue() instanceof Commands.Issue){
      requireThat(require -> {
        require.using("No inputs should be consumed when emitting the ownership", tx.getInputs().isEmpty());
        require.using("Only one output should be produced", tx.getOutputs().size() == 1);

        // Gets the Output state instance from transaction
        final OwnershipState out = tx.outputsOfType(OwnershipState.class).get(FIRST);

        // Non zero ownership value
        require.using("The value of ownershitp should be greater than zero", out.getValue() > 0.0d);

        // Get participants of transaction
        List<?> participants = out.getParticipants().stream().map(AbstractParty::getOwningKey).collect(Collectors.toList());
        
        // Produce log entries
        LoggerFactory.getLogger(OwnershipContract.class).info(participants.toString());
        LoggerFactory.getLogger(OwnershipContract.class).info(command.getSigners().toString());

        require.using("All parties must be signers",
                      command.getSigners().containsAll(participants));

        return null;
      });

      // Transfering (selling) the ownership to another party
    } else if(command.getValue() instanceof Commands.Transfer){
      requireThat(require -> {

        LoggerFactory.getLogger(OwnershipContract.class).info(" > > > > INPUT " + tx.getInputs());
        LoggerFactory.getLogger(OwnershipContract.class).info(" > > > > OUTPUT" + tx.getOutputs());

        // The input with current owner
        require.using("Exactly one input should be provided", tx.getInputs().size() == 1);

        // The output with new onwer
        require.using("Only one output should be produced", tx.getOutputs().size() == 1);

        // Gets the Input state with current owner
        final OwnershipState in = tx.inputsOfType(OwnershipState.class).get(FIRST);

        // Gets the Output state with new owner
        final OwnershipState out = tx.outputsOfType(OwnershipState.class).get(FIRST);

        // Buyer and Seller should not be the same
        require.using("The new owner and old one should not be the same", !in.getOwner().equals(out.getOwner()));

        return null;
      });
    } else {
      throw new IllegalArgumentException("Unknow command: " + command.getValue());
    }
  }

  public interface Commands extends CommandData {
    /*
     * Emits the ownership
     */
    class Issue implements Commands {}

    /*
     * Sells the ownership to another party
     */
    class Transfer implements Commands {}
  }
}
