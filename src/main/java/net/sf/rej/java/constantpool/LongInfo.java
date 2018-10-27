package net.sf.rej.java.constantpool;

import net.sf.rej.util.ByteSerializer;

/**
 * Class that represents a Long info entry in the constant pool.
 * 
 * @author Sami Koivu
 */

public class LongInfo extends ConstantPoolInfo {
    private long value;

    public LongInfo(long value, ConstantPool pool) {
    	super(LONG, pool);
    	this.value = value;
    }
    
    public LongInfo(int highBytes, int lowBytes, ConstantPool pool) {
        super(LONG, pool);
        this.value = (highBytes << 32) + lowBytes;
    }

    @Override
	public String toString() {
        return "(long) " + String.valueOf(this.value);
    }

    @Override
	public byte[] getData() {
        ByteSerializer ser = new ByteSerializer(true);
        ser.addByte(getType());
        ser.addInt((int)((this.value >> 32) & 0xFFFFFFFF));
        ser.addInt((int)(this.value & 0xFFFFFFFF));

        return ser.getBytes();
    }

    @Override
	public int hashCode() {
        return (int) this.value;
    }

    @Override
	public boolean equals(Object other) {
        if (other == null) return false;
        
        try {
            return this.value == ((LongInfo) other).value;
        } catch (ClassCastException cce) {
            return false;
        }
    }

    @Override
	public String getTypeString() {
        return "Long constant";
    }

    public long getLongValue() {
        return this.value;
    }

    public void setLongValue(long newValue) {
        this.value = newValue;
    }

}