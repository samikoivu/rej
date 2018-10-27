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

public class LookupSwitchPig {
	public static void main(String[] args) {
		switch(Integer.parseInt("1")) {
		case 12:
			System.out.println(12);
			break;
		case 999:
			System.out.println(999);
			break;
		case 1054:
			System.out.println(1054);
			break;
		case 999999:
			System.out.println(999999);
			break;
		default:
			System.out.println("Default");
		}
	}
}
