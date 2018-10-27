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

import java.io.UnsupportedEncodingException;

import net.sf.rej.util.ByteArrayByteParser;
import net.sf.rej.util.ByteParser;

public class Android {

	public DexFile readDEX(byte[] data) {
		DexFile dex = new DexFile();

		ByteParser parser = new ByteArrayByteParser(data);
		parser.setBigEndian(false);

		parser.getBytes(8); // magic
		dex.setChecksum(parser.getInt()); // adler32
		dex.setSha1(parser.getBytes(20));
		dex.setFileSize(parser.getInt());
		dex.setHeaderSize(parser.getInt()); // currently 0x70
		dex.setEndianTag(parser.getInt());
		dex.setLinkSize(parser.getInt());
		dex.setLinkOff(parser.getInt());
		dex.setMapOff(parser.getInt());
		dex.setStringIdsSize(parser.getInt());
		dex.setStringIdsOff(parser.getInt());
		dex.setTypeIdsSize(parser.getInt());
		dex.setTypeIdsOff(parser.getInt());
		dex.setProtoIdsSize(parser.getInt());
		dex.setProtoIdsOff(parser.getInt());
		dex.setFieldIdsSize(parser.getInt());
		dex.setFieldIdsOff(parser.getInt());
		dex.setMethodIdsSize(parser.getInt());
		dex.setMethodIdsOff(parser.getInt());
		dex.setClassDefsSize(parser.getInt());
		dex.setClassDefsOff(parser.getInt());
		dex.setDataSize(parser.getInt());
		dex.setDataOff(parser.getInt());
		
		//System.out.println(dex);
		
		// TODO: map

		// links - not used

		// string ids
		parser = new ByteArrayByteParser(data, dex.getStringIdsOff());
		parser.setBigEndian(false);
		for (int i=0; i < dex.getStringIdsSize(); i++) {
			int stringid = parser.getInt();
			ByteParser stringParser = new ByteArrayByteParser(data, stringid);
			
		}
		
		// type ids
		
		// proto ids
		
		// field ids
		
		// method ids
		
		// class defs
		
		// data
		return dex;
	}

	public BinaryXMLFile readXML(byte[] data) {
//		new Exception("Parsing XML").printStackTrace();
		System.out.println("Parsing XML");
		BinaryXMLFile xml = new BinaryXMLFile();

		ByteParser parser = new ByteArrayByteParser(data);
		parser.setBigEndian(false);
		int magic = parser.getInt(); // magic
		if (magic != BinaryXMLFile.MAGIC) throw new RuntimeException("Invalid Binary XML magic: " + magic);
		parser.getInt(); // file size
		xml.setMuggle(parser.getInt()); // unknown: flags, or two shorts
		xml.setxxxSectionOffset(parser.getInt()); //this+8 = start of unknown section between string table and tag data
		int stringCount = parser.getInt();
		xml.setMuggle1(parser.getInt()); // Unknown, 0
		xml.setMuggle2(parser.getInt()); // Unknown, 0
		parser.getInt(); // this + 8 = start of string table
		xml.setMuggle4(parser.getInt()); // unknown, 0

		// @ start of string index table
		int sitOffset = 0x24;
		if (parser.getPosition() != sitOffset) throw new RuntimeException("sitOffset wrong");
		int[] sits = new int[stringCount];
		for (int i=0; i < stringCount; i++) {
			sits[i] = parser.getInt();
		}
		
		// @ start of string table
		xml.setStringCount(stringCount);
		for (int i=0; i < stringCount; i++) {
			int strLen = parser.getShortAsInt();
			byte[] strData = parser.getBytes(strLen * 2);
			parser.getShortAsInt(); // zero byte(s)
			try {
				String str = new String(strData, "UTF-16LE");
				System.out.println("[" + i + "]=" + str);
				xml.addString(str);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		// align by 4
		parser.getBytes(parser.getPosition() % 4);
		
		int xxxSectionMarker = parser.peekInt();
		if (xxxSectionMarker == BinaryXMLFile.XXXMAGIC) {
			parser.getInt(); // skip the part we just peeked
			xml.setXxxSectionSize(parser.getInt());
			xml.setXxxSectionData(parser.getBytes(xml.getXxxSectionSize()-8));
		}
		
		// start of tag data
		XMLElement currentTag = null;
		while (parser.hasMore()) {
			int nextTag = parser.getInt();
			
			if (nextTag == XMLElement.START_NAMESPACE) {
				Namespace ns = new Namespace();
				ns.setMuggle10(parser.getInt()); // low ints
				ns.setSrcLine(parser.getInt());
				ns.setMuggle11(parser.getInt()); // FFFFFFFF
				ns.setNsAlias(parser.getInt());
				ns.setNs(parser.getInt());
				
				ns.setXML(xml);

				if (currentTag == null) {
					xml.setDocumentRoot(ns);
				} else {
					ns.setParent(currentTag);
					currentTag.addChild(ns);
				}
				
				currentTag = ns;
			} else 	if (nextTag == XMLElement.START_TAG) {
				Tag tag = new Tag();
				tag.setXML(xml);
				if (currentTag == null) {
					xml.setDocumentRoot(tag);
				} else {
					tag.setParent(currentTag);
					currentTag.addChild(tag);
				}
				currentTag = tag;
				
				tag.setMuggle10(parser.getInt()); // low ints
				tag.setSrcLine(parser.getInt());
				tag.setMuggle11(parser.getInt()); // FFFFFFFF
				tag.setNs(parser.getInt());
				tag.setName(parser.getInt());

				tag.setMuggle12(parser.getInt());
				int attrCount = parser.getInt();
				tag.setMuggle13(parser.getInt());
				
				for (int i=0; i < attrCount; i++) {
					Attribute attr = new Attribute();
					tag.add(attr);
					attr.setXML(xml);
					attr.setNs(parser.getInt());
					attr.setName(parser.getInt());
					attr.setValue(parser.getInt());
					attr.setMuggle14(parser.getInt());
					attr.setResourceId(parser.getInt());
					attr.setParent(tag);
					
				}
			} else if (nextTag == XMLElement.END_TAG) {
				Tag tag = (Tag) currentTag;
				tag.setE_muggle10(parser.getInt()); // low ints
				tag.setE_srcLine(parser.getInt());
				tag.setE_muggle11(parser.getInt());
				int ns = parser.getInt();
				int name = parser.getInt();

				if (ns != tag.getNs() || name != tag.getName()) {
					throw new RuntimeException("Tag mismatch");
				}
				currentTag = currentTag.getParent();
			} else if (nextTag == XMLElement.END_NAMESPACE) {
				Namespace namespace = (Namespace) currentTag;
				namespace.setE_muggle10(parser.getInt()); // low ints
				namespace.setE_srcLine(parser.getInt());
				namespace.setE_muggle11(parser.getInt());

				int alias = parser.getInt();
				int ns = parser.getInt();
				if (ns != currentTag.getNs() || alias != namespace.getNsAlias()) {
					throw new RuntimeException("Namespace end mismatch");
				}
				currentTag = currentTag.getParent();
			} else if (nextTag == XMLElement.TAG_DATA) {
				TagData td = new TagData();
				td.setXML(xml);
				td.setMuggle10(parser.getInt());
				td.setSrcLine(parser.getInt());
				td.setMuggle11(parser.getInt());
				td.setValue(parser.getInt());
				td.setMuggle12(parser.getInt());
				td.setNs(parser.getInt());

				td.setParent(currentTag);
				currentTag.addChild(td);
			} else {
				throw new RuntimeException("Invalid tag!");
			}
		}
		
		return xml;
	}

}
