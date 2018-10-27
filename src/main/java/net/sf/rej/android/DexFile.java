/* Copyright (C) 2004-2011 Sami Koivu
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.rej.android;

import java.util.Arrays;

import net.sf.rej.gui.FileObject;

public class DexFile implements FileObject {

	@Override
	public String toString() {
		return "DexFile [checksum=" + checksum + ", sha1="
				+ Arrays.toString(sha1) + ", fileSize=" + fileSize
				+ ", headerSize=" + headerSize + ", endianTag=" + endianTag
				+ ", linkSize=" + linkSize + ", linkOff=" + linkOff
				+ ", mapOff=" + mapOff + ", stringIdsSize=" + stringIdsSize
				+ ", stringIdsOff=" + stringIdsOff + ", typeIdsSize="
				+ typeIdsSize + ", typeIdsOff=" + typeIdsOff
				+ ", protoIdsSize=" + protoIdsSize + ", protoIdsOff="
				+ protoIdsOff + ", fieldIdsSize=" + fieldIdsSize
				+ ", fieldIdsOff=" + fieldIdsOff + ", methodIdsSize="
				+ methodIdsSize + ", methodIdsOff=" + methodIdsOff
				+ ", classDefsSize=" + classDefsSize + ", classDefsOff="
				+ classDefsOff + ", dataSize=" + dataSize + ", dataOff="
				+ dataOff + "]";
	}

	private int checksum;
	private byte[] sha1;
	private int fileSize;
	private int headerSize;
	private int endianTag;
	private int linkSize;
	private int linkOff;
	private int mapOff;
	private int stringIdsSize;
	private int stringIdsOff;
	private int typeIdsSize;
	private int typeIdsOff;
	private int protoIdsSize;
	private int protoIdsOff;
	private int fieldIdsSize;
	private int fieldIdsOff;
	private int methodIdsSize;
	private int methodIdsOff;
	private int classDefsSize;
	private int classDefsOff;
	private int dataSize;
	private int dataOff;

	public byte[] getData() {
		return null;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public byte[] getSha1() {
		return sha1;
	}

	public void setSha1(byte[] sha1) {
		this.sha1 = sha1;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

	public int getEndianTag() {
		return endianTag;
	}

	public void setEndianTag(int endianTag) {
		this.endianTag = endianTag;
	}

	public int getLinkOff() {
		return linkOff;
	}

	public void setLinkOff(int linkOff) {
		this.linkOff = linkOff;
	}

	public int getMapOff() {
		return mapOff;
	}

	public void setMapOff(int mapOff) {
		this.mapOff = mapOff;
	}

	public int getStringIdsSize() {
		return stringIdsSize;
	}

	public void setStringIdsSize(int stringIdsSize) {
		this.stringIdsSize = stringIdsSize;
	}

	public int getStringIdsOff() {
		return stringIdsOff;
	}

	public void setStringIdsOff(int stringIdsOff) {
		this.stringIdsOff = stringIdsOff;
	}

	public int getTypeIdsSize() {
		return typeIdsSize;
	}

	public void setTypeIdsSize(int typeIdsSize) {
		this.typeIdsSize = typeIdsSize;
	}

	public int getTypeIdsOff() {
		return typeIdsOff;
	}

	public void setTypeIdsOff(int typeIdsOff) {
		this.typeIdsOff = typeIdsOff;
	}

	public int getProtoIdsSize() {
		return protoIdsSize;
	}

	public void setProtoIdsSize(int protoIdsSize) {
		this.protoIdsSize = protoIdsSize;
	}

	public int getProtoIdsOff() {
		return protoIdsOff;
	}

	public void setProtoIdsOff(int protoIdsOff) {
		this.protoIdsOff = protoIdsOff;
	}

	public int getFieldIdsSize() {
		return fieldIdsSize;
	}

	public void setFieldIdsSize(int fieldIdsSize) {
		this.fieldIdsSize = fieldIdsSize;
	}

	public int getFieldIdsOff() {
		return fieldIdsOff;
	}

	public void setFieldIdsOff(int fieldIdsOff) {
		this.fieldIdsOff = fieldIdsOff;
	}

	public int getMethodIdsSize() {
		return methodIdsSize;
	}

	public void setMethodIdsSize(int methodIdsSize) {
		this.methodIdsSize = methodIdsSize;
	}

	public int getMethodIdsOff() {
		return methodIdsOff;
	}

	public void setMethodIdsOff(int methodIdsOff) {
		this.methodIdsOff = methodIdsOff;
	}

	public int getClassDefsSize() {
		return classDefsSize;
	}

	public void setClassDefsSize(int classDefsSize) {
		this.classDefsSize = classDefsSize;
	}

	public int getClassDefsOff() {
		return classDefsOff;
	}

	public void setClassDefsOff(int classDefsOff) {
		this.classDefsOff = classDefsOff;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getDataOff() {
		return dataOff;
	}

	public void setDataOff(int dataOff) {
		this.dataOff = dataOff;
	}

	public int getLinkSize() {
		return linkSize;
	}

	public void setLinkSize(int linkSize) {
		this.linkSize = linkSize;
	}

}
