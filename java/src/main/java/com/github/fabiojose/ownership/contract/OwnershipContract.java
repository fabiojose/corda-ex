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

public class OwnershipContract implements Contract {
  public static final String OWNERSHIP_CONTRACT_ID = "com.github.fabiojose.ownership.contract.OwnershipContract";

  private static final int FIRST = 0;

  @Override
  public void verify(LedgerTransaction tx) {
    final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

    if(command.getValue() instanceof Commands.Issue){
      // Emiting the ownership record
      requireThat(require -> {
        require.using("No inputs should be consumed when emitting the ownership", tx.getInputs().isEmpty());
        require.using("Only one output should be produced", tx.getOutputs().size() == 1);

        // Gets the Output state instance from transaction
        final OwnershipState out = tx.outputsOfType(OwnershipState.class).get(FIRST);

        // Non zero ownership valure
        require.using("The value of ownershitp should be greater than zero", out.getValue() > 0.0d);

        // Get participants of transaction
        List<?> participants = out.getParticipants().stream().map(AbstractParty::getOwningKey).collect(Collectors.toList());
        require.using("All parties must be signers",
                     command.getSigners().contains(participants));

        return null;
      });
    } else if(command.getValue() instanceof Commands.Transfer){

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
