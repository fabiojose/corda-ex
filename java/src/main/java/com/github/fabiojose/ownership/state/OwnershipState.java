package com.github.fabiojose.ownership.state;

import com.github.fabiojose.ownership.schema.OwnershipSchemaV1;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.LinearState;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.QueryableState;
import net.corda.core.schemas.PersistentState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.contracts.UniqueIdentifier;

import java.util.Arrays;
import java.util.List;

/*
 * The ownership state class definition.
 * Implements LinearState that guarantee only one current version of ownership
 * Implements QueryableState to allow queries on the node's database
 */
public class OwnershipState implements LinearState, QueryableState {

  private final String object;
  private final String description;

  private final Double value;
  private final String currency;

  private final Party owner;

  private final UniqueIdentifier linearId;

  public OwnershipState(String object, String description, Double value, String currency, Party owner, UniqueIdentifier linearId){
    this.object = object;
    this.description = description;
    this.value = value;
    this.currency = currency;
    this.owner = owner;
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
    if(schema instanceof OwnershipSchemaV1){
      return new OwnershipSchemaV1
                  .PersistentOwnership(object,
                                       description,
                                       value,
                                       currency,
                                       owner.getName().toString(),
                                       linearId.getId());
    } else {
      throw new IllegalArgumentException(String.format("Unrecognized schema: %s", schema));
    }
  }

  @Override
  public Iterable<MappedSchema> supportedSchemas(){
    return ImmutableList.of(new OwnershipSchemaV1());
  }

  public String toString(){
    return String.format("Ownership(object=%s, value=%s, currency=%s, owner=%s)", object, value, currency, owner);
  }
}
