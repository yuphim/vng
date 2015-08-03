/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package vng.zingme.payment.thrift;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.thrift.*;
import org.apache.thrift.async.*;
import org.apache.thrift.meta_data.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;

public class T_VerifyCardResponse implements TBase<T_VerifyCardResponse, T_VerifyCardResponse._Fields>, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("T_VerifyCardResponse");

  private static final TField RESCODE_FIELD_DESC = new TField("rescode", TType.I32, (short)1);
  private static final TField TRANSACTION_ID_FIELD_DESC = new TField("transactionID", TType.I64, (short)2);

  public int rescode;
  public long transactionID;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements TFieldIdEnum {
    RESCODE((short)1, "rescode"),
    TRANSACTION_ID((short)2, "transactionID");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // RESCODE
          return RESCODE;
        case 2: // TRANSACTION_ID
          return TRANSACTION_ID;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __RESCODE_ISSET_ID = 0;
  private static final int __TRANSACTIONID_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);

  public static final Map<_Fields, FieldMetaData> metaDataMap;
  static {
    Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RESCODE, new FieldMetaData("rescode", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.I32)));
    tmpMap.put(_Fields.TRANSACTION_ID, new FieldMetaData("transactionID", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    FieldMetaData.addStructMetaDataMap(T_VerifyCardResponse.class, metaDataMap);
  }

  public T_VerifyCardResponse() {
  }

  public T_VerifyCardResponse(
    int rescode,
    long transactionID)
  {
    this();
    this.rescode = rescode;
    setRescodeIsSet(true);
    this.transactionID = transactionID;
    setTransactionIDIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public T_VerifyCardResponse(T_VerifyCardResponse other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.rescode = other.rescode;
    this.transactionID = other.transactionID;
  }

  public T_VerifyCardResponse deepCopy() {
    return new T_VerifyCardResponse(this);
  }

  @Override
  public void clear() {
    setRescodeIsSet(false);
    this.rescode = 0;
    setTransactionIDIsSet(false);
    this.transactionID = 0;
  }

  public int getRescode() {
    return this.rescode;
  }

  public T_VerifyCardResponse setRescode(int rescode) {
    this.rescode = rescode;
    setRescodeIsSet(true);
    return this;
  }

  public void unsetRescode() {
    __isset_bit_vector.clear(__RESCODE_ISSET_ID);
  }

  /** Returns true if field rescode is set (has been asigned a value) and false otherwise */
  public boolean isSetRescode() {
    return __isset_bit_vector.get(__RESCODE_ISSET_ID);
  }

  public void setRescodeIsSet(boolean value) {
    __isset_bit_vector.set(__RESCODE_ISSET_ID, value);
  }

  public long getTransactionID() {
    return this.transactionID;
  }

  public T_VerifyCardResponse setTransactionID(long transactionID) {
    this.transactionID = transactionID;
    setTransactionIDIsSet(true);
    return this;
  }

  public void unsetTransactionID() {
    __isset_bit_vector.clear(__TRANSACTIONID_ISSET_ID);
  }

  /** Returns true if field transactionID is set (has been asigned a value) and false otherwise */
  public boolean isSetTransactionID() {
    return __isset_bit_vector.get(__TRANSACTIONID_ISSET_ID);
  }

  public void setTransactionIDIsSet(boolean value) {
    __isset_bit_vector.set(__TRANSACTIONID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case RESCODE:
      if (value == null) {
        unsetRescode();
      } else {
        setRescode((Integer)value);
      }
      break;

    case TRANSACTION_ID:
      if (value == null) {
        unsetTransactionID();
      } else {
        setTransactionID((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case RESCODE:
      return new Integer(getRescode());

    case TRANSACTION_ID:
      return new Long(getTransactionID());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case RESCODE:
      return isSetRescode();
    case TRANSACTION_ID:
      return isSetTransactionID();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof T_VerifyCardResponse)
      return this.equals((T_VerifyCardResponse)that);
    return false;
  }

  public boolean equals(T_VerifyCardResponse that) {
    if (that == null)
      return false;

    boolean this_present_rescode = true;
    boolean that_present_rescode = true;
    if (this_present_rescode || that_present_rescode) {
      if (!(this_present_rescode && that_present_rescode))
        return false;
      if (this.rescode != that.rescode)
        return false;
    }

    boolean this_present_transactionID = true;
    boolean that_present_transactionID = true;
    if (this_present_transactionID || that_present_transactionID) {
      if (!(this_present_transactionID && that_present_transactionID))
        return false;
      if (this.transactionID != that.transactionID)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(T_VerifyCardResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    T_VerifyCardResponse typedOther = (T_VerifyCardResponse)other;

    lastComparison = Boolean.valueOf(isSetRescode()).compareTo(typedOther.isSetRescode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRescode()) {
      lastComparison = TBaseHelper.compareTo(this.rescode, typedOther.rescode);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTransactionID()).compareTo(typedOther.isSetTransactionID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTransactionID()) {
      lastComparison = TBaseHelper.compareTo(this.transactionID, typedOther.transactionID);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(TProtocol iprot) throws TException {
    TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // RESCODE
          if (field.type == TType.I32) {
            this.rescode = iprot.readI32();
            setRescodeIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // TRANSACTION_ID
          if (field.type == TType.I64) {
            this.transactionID = iprot.readI64();
            setTransactionIDIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    // check for required fields of primitive type, which can't be checked in the validate method
    validate();
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    oprot.writeFieldBegin(RESCODE_FIELD_DESC);
    oprot.writeI32(this.rescode);
    oprot.writeFieldEnd();
    oprot.writeFieldBegin(TRANSACTION_ID_FIELD_DESC);
    oprot.writeI64(this.transactionID);
    oprot.writeFieldEnd();
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("T_VerifyCardResponse(");
    boolean first = true;

    sb.append("rescode:");
    sb.append(this.rescode);
    first = false;
    if (!first) sb.append(", ");
    sb.append("transactionID:");
    sb.append(this.transactionID);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
  }

}
