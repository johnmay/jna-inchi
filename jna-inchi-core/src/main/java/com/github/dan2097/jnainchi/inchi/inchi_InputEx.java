/**
 * JNA-InChI - Library for calling InChI from Java
 * Copyright © 2018 Daniel Lowe
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.dan2097.jnainchi.inchi;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.Structure.ByReference;
/**
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class inchi_InputEx extends Structure implements ByReference {
  /**
   * array of num_atoms elements<br>
   * C type : inchi_Atom*
   */
  public tagInchiAtom atom;
  /**
   * array of num_stereo0D 0D stereo elements or NULL<br>
   * C type : inchi_Stereo0D*
   */
  public tagINCHIStereo0D stereo0D;
  /**
   * InChI options: space-delimited; each is preceded by<br>
   * C type : char*
   */
  public String szOptions;
  /**
   * number of atoms in the structure<br>
   * C type : AT_NUM
   */
  public short num_atoms;
  /**
   * number of 0D stereo elements<br>
   * C type : AT_NUM
   */
  public short num_stereo0D;
  /**
   * v. 1.05+ extended data, polymers<br>
   * C type : inchi_Input_Polymer*
   */
  public inchi_Input_Polymer polymer;
  /**
   * v. 1.05+ extended data, V3000 Molfile features<br>
   * C type : inchi_Input_V3000*
   */
  public inchi_Input_V3000 v3000;

  protected List<String> getFieldOrder() {
    return Arrays.asList("atom", "stereo0D", "szOptions", "num_atoms", "num_stereo0D", "polymer", "v3000");
  }
  /**
   * @param atom array of num_atoms elements<br>
   * C type : inchi_Atom*<br>
   * @param stereo0D array of num_stereo0D 0D stereo elements or NULL<br>
   * C type : inchi_Stereo0D*<br>
   * @param szOptions InChI options: space-delimited; each is preceded by<br>
   * C type : char*<br>
   * @param num_atoms number of atoms in the structure<br>
   * C type : AT_NUM<br>
   * @param num_stereo0D number of 0D stereo elements<br>
   * C type : AT_NUM<br>
   * @param polymer v. 1.05+ extended data, polymers<br>
   * C type : inchi_Input_Polymer*<br>
   * @param v3000 v. 1.05+ extended data, V3000 Molfile features<br>
   * C type : inchi_Input_V3000*
   */
  public inchi_InputEx(tagInchiAtom atom, tagINCHIStereo0D stereo0D, String szOptions, short num_atoms, short num_stereo0D, inchi_Input_Polymer polymer, inchi_Input_V3000 v3000) {
    super();
    this.atom = atom;
    this.stereo0D = stereo0D;
    this.szOptions = szOptions;
    this.num_atoms = num_atoms;
    this.num_stereo0D = num_stereo0D;
    this.polymer = polymer;
    this.v3000 = v3000;
  }

}
