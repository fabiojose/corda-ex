package com.github.fabiojose.ownership.state;

import net.corda.core.contracts.LinearState;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.QueryableState;
import net.corda.core.schemas.PersistentState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.contracts.UniqueIdentifier;

import java.util.Arrays;
import java.util.List;

public class OwnershipState implements LinearState, QueryableState {

  private final String object;
  private final String description;

  private final Double value;
  private final String currency;

  private final Party owner;

  private final Party buyer;

  private final UniqueIdentifier linearId;

  public OwnershipState(String object, String description, Double value, String currency, Party owner, Party buyer, UniqueIdentifier linearId){
    this.object = object;
    this.description = description;
    this.value = value;
    this.currency = currency;
    this.owner = owner;
    this.buyer = buyer;
    this.linearId = linearId;
  } 

  public String getObject(){
    return object;
  }

  public String getDescription(){
    return description;
  }

  public Double getValue(){
    return value;
  }

  public String getCurrency(){
    return currency;
  }

  public Party getOwner(){
    return owner;
  }

  public Party getBuyer(){
    return buyer;
  }

  @Override
  public UniqueIdentifier getLinearId(){
    return linearId;
  }

  @Override
  public List<AbstractParty> getParticipants(){
    return Arrays.asList(owner);
  }

  @Override
  public PersistentState generateMappedObject(MappedSchema schema){
    return null;
  }

  @Override
  public Iterable<MappedSchema> supportedSchemas(){
    return null;
  }
}
