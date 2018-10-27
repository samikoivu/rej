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
package net.sf.rej.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.rej.files.ClassIndex;
import net.sf.rej.files.ClassLocator;
import net.sf.rej.files.MethodLocator;

public class ClassHierarchy {

    /**
     * Classes, starting from the current class in the index 0
     * with the superclass of the current class in the index 1
     * and so on.
     */
    private List<ClassFile> classes = new ArrayList<ClassFile>();

    /**
     * All the interfaces implemented by this class or it's
     * superclasses.
     */
    private List<ClassFile> interfaces = new ArrayList<ClassFile>();

    private ClassIndex classIndex;

    private ClassHierarchy(ClassIndex classIndex) {
        this.classIndex = classIndex;
    }

    public static ClassHierarchy getHierarchy(ClassFile cf, ClassIndex classIndex) throws IOException {
        // TODO: This class should recover from circularity errors gracefully
        ClassHierarchy hierarchy = new ClassHierarchy(classIndex);
        hierarchy.addClass(cf);
        return hierarchy;
    }

    public void addClass(ClassFile cls) throws IOException {
        this.classes.add(cls);
        for (Interface intf : cls.getInterfaces()) {
            ClassFile intfCF = this.classIndex.getByFullName(intf.getName());
            if (intfCF != null) {
                // TODO: Reflect upon whether silent discarding of unfound classes
                // is smart or not.
                addInterface(intfCF);
            }
        }
        String superName = cls.getSuperClassName();
        if (superName != null) {
            ClassFile superClass = this.classIndex.getByFullName(superName);
            if (superClass != null) {
                // TODO: Reflect upon whether silent discarding of unfound classes
                // is smart or not.
                addClass(superClass);
            }
        }

    }

    public void addInterface(ClassFile cf) throws IOException {
        this.interfaces.add(cf);
        for (Interface intf : cf.getInterfaces()) {
            ClassFile intfCF = this.classIndex.getByFullName(intf.getName());
            if (intfCF != null) {
                // TODO: Reflect upon whether silent discarding of unfound classes
                // is smart or not.
                addInterface(intfCF);
            }
        }

    }

    public boolean isOverridden(Method method) {
        for (ClassFile cf : this.classes) {
            if (this.classes.indexOf(cf) == 0) continue; // skip the current class

            for (Method superMethod : cf.getMethods()) {
                if (superMethod.signatureMatches(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isImplemented(Method method) {
        for (ClassFile cf : this.interfaces) {
            if (this.classes.indexOf(cf) == 0) continue; // skip the current interface

            for (Method superMethod : cf.getMethods()) {
                if (superMethod.signatureMatches(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MethodLocator getImplemented(Method method) {
        for (ClassFile cf : this.interfaces) {
            if (this.classes.indexOf(cf) == 0) continue; // skip the current interface

            for (Method superMethod : cf.getMethods()) {
                if (superMethod.signatureMatches(method)) {
                    ClassLocator cl = this.classIndex.getLocator(cf.getFullClassName());
                    return new MethodLocator(cl, superMethod);
                }
            }
        }
        return null;
    }

    public MethodLocator getOverridden(Method method) {
        for (ClassFile cf : this.classes) {
            if (this.classes.indexOf(cf) == 0) continue; // skip the current interface

            for (Method superMethod : cf.getMethods()) {
                if (superMethod.signatureMatches(method)) {
                    ClassLocator cl = this.classIndex.getLocator(cf.getFullClassName());
                    return new MethodLocator(cl, superMethod);
                }
            }
        }
        return null;
    }

}
