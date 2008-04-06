package org.newdawn.commet.object.encoders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import org.newdawn.commet.object.FieldEncoder;

/**
 * Encoder to set a field based on recieved data, and send data based on the state of a field
 * whose type is "char".
 * 
 * @author kevin
 */
public class CharEncoder implements FieldEncoder {
	/** The field to be set/get */
	private Field field;
	
	/**
	 * Create a new encoder
	 * 
	 * @param field The field to be maintained
	 */
	public CharEncoder(Field field) {
		this.field = field;
		field.setAccessible(true);
	}
	
	/**
	 * @see org.newdawn.commet.object.FieldEncoder#decode(java.io.DataInputStream, java.lang.Object)
	 */
	public void decode(DataInputStream din, Object value) throws IOException {
		try {
			field.setChar(value, din.readChar());
		} catch (IllegalArgumentException e) {
			throw new IOException("Failed to set: "+field);
		} catch (IllegalAccessException e) {
			throw new IOException("Failed to set: "+field);
		}
	}

	/**
	 * @see org.newdawn.commet.object.FieldEncoder#encode(java.io.DataOutputStream, java.lang.Object)
	 */
	public void encode(DataOutputStream dout, Object value) throws IOException {
		try {
			dout.writeChar(field.getChar(value));
		} catch (IllegalArgumentException e) {
			throw new IOException("Failed to get: "+field);
		} catch (IllegalAccessException e) {
			throw new IOException("Failed to get: "+field);
		}
	}

	
}