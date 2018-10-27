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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sf.rej.util.Checksum;
import net.sf.rej.util.IOToolkit;

public class DeepArchive extends FileSet {

    private File file = null;
    private List<String> contents = null;
    private JarOutputStream jarOut = null;
    private JarFile jf = null;
    private ArchiveTreeItem root = new ArchiveTreeItem("");

    private DeepArchive() {
        // do-nothing constructor
    }

    public DeepArchive(File f) throws IOException {
        this.file = f;
        refresh();
    }

    @Override
	public List<String> getContentsList() {
    	List<String> list = new ArrayList<String>(this.contents.size());
    	list.addAll(this.contents);
        return list;
    }

    @Override
	public byte[] getData(String file) throws IOException {
        ArchiveTreeItem found = null;
        for (ArchiveTreeItem item : this.root.getChildren()) {
        	if (file.startsWith(item.getName() + "/")) {
        		found = item;
        		break;
        	}
        }

        if (found == null) {
           	JarEntry je = this.jf.getJarEntry(file);
           	InputStream is = this.jf.getInputStream(je);
        	byte[] data = IOToolkit.readStream((int) je.getSize(), is);
        	is.close();
        	return data;
        } else {
           	JarEntry je = this.jf.getJarEntry(found.getName());
           	InputStream is = this.jf.getInputStream(je);
        	return getDataFromArchive(file.substring(found.getName().length() + 1), found, is);
        }
    }

	private byte[] getDataFromArchive(String file, ArchiveTreeItem arch, InputStream is) throws IOException {
        ArchiveTreeItem found = null;
        for (ArchiveTreeItem item : arch.getChildren()) {
        	if (file.startsWith(item.getName() + "/")) {
        		found = item;
        		break;
        	}
        }

        if (found == null) {
    		ZipInputStream zis = new ZipInputStream(is);

    		while (true) {
    			ZipEntry entry = zis.getNextEntry();
    			if (entry == null) break;

    			if (entry.getName().equals(file)) {
    	        	byte[] data = IOToolkit.readStream(zis);
    	        	zis.close();
    	        	return data;
    			}
    		}
    		zis.close();
        } else {
    		ZipInputStream zis = new ZipInputStream(is);

    		while (true) {
    			ZipEntry entry = zis.getNextEntry();
    			if (entry == null) break;

    			if (entry.getName().equals(found.getName())) {
    				try {
    					return getDataFromArchive(file.substring(found.getName().length()+1), found, zis);
    				} finally {
    					zis.close();
    				}

    			}
    		}
    		zis.close();
        }
		throw new RuntimeException("Element not found in archive: " + file);
	}

	private InputStream getStreamFromArchive(String file, ArchiveTreeItem arch, InputStream is) throws IOException {
        ArchiveTreeItem found = null;
        for (ArchiveTreeItem item : arch.getChildren()) {
        	if (file.startsWith(item.getName() + "/")) {
        		found = item;
        		break;
        	}
        }

        if (found == null) {
    		ZipInputStream zis = new ZipInputStream(is);

    		while (true) {
    			ZipEntry entry = zis.getNextEntry();
    			if (entry == null) break;

    			if (entry.getName().equals(file)) {
    				return zis;
    			}
    		}
    		zis.close();
        } else {
    		ZipInputStream zis = new ZipInputStream(is);

    		while (true) {
    			ZipEntry entry = zis.getNextEntry();
    			if (entry == null) break;

    			if (entry.getName().equals(found.getName())) {
    				try {
    					return getStreamFromArchive(file.substring(found.getName().length()+1), found, zis);
    				} finally {
    					zis.close();
    				}

    			}
    		}
    		zis.close();
        }
		throw new RuntimeException("Element not found in archive: " + file);
	}

    @Override
	public String getName() {
        if (this.file != null) {
            return this.file.getName();
        } else {
            return "";
        }
    }

    @Override
	public InputStream getInputStream(String file) throws IOException {
        ArchiveTreeItem found = null;
        for (ArchiveTreeItem item : this.root.getChildren()) {
        	if (file.startsWith(item.getName() + "/")) {
        		found = item;
        		break;
        	}
        }

        if (found == null) {
        	JarEntry je = this.jf.getJarEntry(file);
            return this.jf.getInputStream(je);
        } else {
           	JarEntry je = this.jf.getJarEntry(found.getName());
           	InputStream is = this.jf.getInputStream(je);
        	return getStreamFromArchive(file.substring(found.getName().length() + 1), found, is);
        }

    }

    public void write(String filename, byte[] data) throws IOException {
        ZipEntry ze = new ZipEntry(filename);
        this.jarOut.putNextEntry(ze);
        this.jarOut.write(data);
    }

    public void write(String filename, InputStream is) throws IOException {
        ZipEntry ze = new ZipEntry(filename);
        this.jarOut.putNextEntry(ze);
        IOToolkit.writeStream(is, this.jarOut);
    }

    @Override
	public void close() throws IOException {
        if (this.jf != null) {
            this.jf.close();
        }
    }

    /**
     * Mark file as to be added to archive.
     * @param filename String
     */
    @Override
	public void addFile(String filename) {
        this.contents.add(filename);
    }

    /**
     * Mark file as to-be-removed. On next save it will not be saved.
     * @param filename String
     */
    @Override
	public void removeFile(String filename) {
        this.contents.remove(filename);
    }

    /*
     * @see net.sourceforge.rejava.gui.FileSet#refresh()
     */
    @Override
	public void refresh() throws IOException {
        this.contents = new ArrayList<String>();
        if (this.file.length() == 0) return;

        if (this.jf != null) this.jf.close();

        this.jf = new JarFile(this.file);
        Enumeration e = this.jf.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = (JarEntry) e.nextElement();
            if (entry.isDirectory()) {
            	// no treatment for dirs (this way, though, empty dirs will be removed from archive when saving)
            } else if (entry.getName().endsWith(".zip")
            		|| entry.getName().endsWith(".jar")
            		|| entry.getName().endsWith(".ear")
            		|| entry.getName().endsWith(".war")
            		|| entry.getName().endsWith(".rar")) {
            	ArchiveTreeItem item = new ArchiveTreeItem(entry.getName());
            	this.root.addChild(item);
            	addContents(this.jf.getInputStream(entry), entry.getName(), item);
            } else {
                this.contents.add(entry.getName());
            }
        }

    }

    private void addContents(InputStream is, String prefix, ArchiveTreeItem item) {
    	try {
    		ZipInputStream zis = new ZipInputStream(is);

    		while (true) {
    			ZipEntry entry = zis.getNextEntry();
    			if (entry == null) break;

                if (entry.isDirectory()) {
                	// no treatment for dirs (this way, though, empty dirs will be removed from archive when saving)
                } else if (entry.getName().endsWith(".zip")
                		|| entry.getName().endsWith(".jar")
                		|| entry.getName().endsWith(".ear")
                		|| entry.getName().endsWith(".war")
                		|| entry.getName().endsWith(".rar")) {
                	ArchiveTreeItem child = new ArchiveTreeItem(entry.getName());
                	item.addChild(child);
                	addContents(zis, prefix + "/" + entry.getName(), child);
                } else {
                    this.contents.add(prefix + "/" + entry.getName());
                }

    		}

    		zis.close();
    	} catch(Exception ex) {
    		// in the case of an error, let's just add the file as a file
    		// without trying to "go deep"
    		ex.printStackTrace();
    		this.contents.add(prefix);
    	}
    }

    @Override
	public void save(Modifications mods) throws IOException {
    	File tempFile = File.createTempFile("rejava", "temp", this.file.getParentFile());
        saveAs(tempFile, mods);
        this.close();
        boolean success = this.file.delete();
        if (!success) {
        	throw new IOException("Could not remove file " + this.file.getPath() + " to write the new file.");
        }

        success = tempFile.renameTo(this.file);
        if (!success) {
        	throw new IOException("Could not rename file. New file saved as " + tempFile.getPath() + " instead.");
        }

        refresh();
    }

    @Override
	public void saveAs(File file, Modifications mods) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, false);
        if(this.contents.size() == 0) {
            fos.close();
            return;
        }

        this.jarOut = new JarOutputStream(fos);

        for (int i = 0; i < this.contents.size(); i++) {
            String filename = this.contents.get(i);
            if (mods.isModified(filename)) {
                byte[] data = mods.getData(filename);
                write(filename, data);
            } else {
            	InputStream in = getInputStream(filename);
                write(filename, in);
                in.close();
            }
        }

        this.jarOut.flush();
        this.jarOut.close();
        fos.close();
    }

    public void partialSave(String filename, byte[] data) throws IOException {
        throw new IOException("Archive type does not support partial save. File " + filename + " was not saved.");
    }

    public static DeepArchive createNew(File file) throws IOException {
        DeepArchive archive = new DeepArchive();
        archive.file = file;
        archive.contents = new ArrayList<String>();
        file.createNewFile();
        return archive;
    }

	@Override
	public void removeAllFiles() throws IOException {
		this.contents.clear();
	}

	@Override
	public String getClasspath(String mainClass) {
		return this.file.getPath();
	}

	@Override
	public long getChecksum() throws IOException {
		Checksum checksum = new Checksum();
		FileInputStream fis = new FileInputStream(this.file);
		checksum.updateCRCWithStream(fis);
		fis.close();

		return checksum.getChecksum();
	}
}
