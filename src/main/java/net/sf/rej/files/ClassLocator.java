/* Copyright (C) 2004-2007 Sami Koivu
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
package net.sf.rej.files;

import java.io.Serializable;


public class ClassLocator implements Serializable {
	public static final long serialVersionUID = 2;

    private String fullName = null;
    private String file = null;

    /**
     * When deserializing, fileSet will be set explicitly
     */
    transient private FileSet fileSet = null;

    public ClassLocator(String classLongName, FileSet fileSet, String file) {
    	this.fullName = classLongName;
        this.fileSet = fileSet;
        this.file = file;
    }

    public String getShortName() {
    	int dot = this.fullName.lastIndexOf(".");
    	if (dot == -1) {
    		return this.fullName;
    	} else {
    		return this.fullName.substring(dot + 1);
    	}
    }

    public String getPackage() {
    	int dot = this.fullName.lastIndexOf(".");
        if (dot == -1) {
        	return "";
        } else {
        	return this.fullName.substring(0, dot);
        }
    }

    public String getFile() {
        return this.file;
    }

    public FileSet getFileSet() {
        return this.fileSet;
    }

    public void setFileSet(FileSet fs) {
    	this.fileSet = fs;
    }

    @Override
	public String toString() {
        return getShortName();
    }

    public String getFullName() {
    	return this.fullName;
    }

    public String dumpDetails() {
        return "File set: " + this.getFileSet() + ", File: " + this.getFile() + ", Class name: " + this.getFullName();
    }

}
