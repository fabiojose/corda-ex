package com.github.fabiojose.ownership.schema;

import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.UUID;

/*
 * The ownership schema definition master class.
 */
public class OwnershipSchemaV1 extends MappedSchema {

  public OwnershipSchemaV1(){
    super(
          OwnershipSchema.class,                      // The schema group
          1,                                          // The schema version
          ImmutableList.of(PersistentOwnership.class) // The list of ORM entities
         );
  }

  /*
   * The ORM to persist the ownership records.
   */
  @Entity
  @Table(name = "ownership_states")
  public static class PersistentOwnership extends PersistentState{

    // the object: a car, a house, a land, a motorcycle, and so on ...
    @Column(name = "object")
    private final String object;

    // the object's description
    @Column(name = "description")
    private final String description;

    // the value of object
    @Column(name = "value")
    private final Double value;

    // the currency of value
    @Column(name = "currency")
    private final String currency;

    // the owner of object
    @Column(name = "owner")
    private final String owner;

    // field to save the linear id for LinearState
    @Column(name = "linear_id")
    private final UUID linearId;

    public PersistentOwnership(String object, String description, Double value, String currency, String owner, UUID linearId){
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

    public String getCurrency() {
      return currency;
    }

    public String getOwner() {
      return owner;
    }

    public UUID getLinearId(){
      return linearId;
    }
  }
}
