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
package net.sf.rej.guineapigs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import javax.swing.JFrame;

public class SerializedPig implements Serializable {
    private String key = "secret";
    private int number = 1;
    private SerializedPig other;

    public static void main(String[] args) {
        try {
            SerializedPig list1 = new SerializedPig();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SerializedPig.class.getName()));
            out.writeObject(Calendar.getInstance());
            out.flush();
        	/*ObjectInputStream in = new ObjectInputStream(new FileInputStream("mod2"));
        	SerializedPig pig = (SerializedPig) in.readObject();
        	System.out.println(pig.number);
        	System.out.println(pig.other.number);
        	in.close();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class SerializedPigParent implements Serializable {
	public String parentString = "asdf";
	public int parentInt = 91;
}
