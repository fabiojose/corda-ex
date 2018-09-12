package com.github.fabiojose.ownership.contract;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;

import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

public class OwnershipContract implements Contract {
  public static final String OWNERSHIP_CONTRACT_ID = "com.github.fabiojose.ownership.contract.OwnershipContract";

  @Override
  public void verify(LedgerTransaction tx) {
    final CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

    if(command.getValue() instanceof Commands.Issue){

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
