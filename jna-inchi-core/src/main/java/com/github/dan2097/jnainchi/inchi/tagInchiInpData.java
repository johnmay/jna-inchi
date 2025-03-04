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
public class tagInchiInpData extends Structure implements ByReference {
  /**
   * a pointer to pInp that has all items 0 or NULL<br>
   * C type : inchi_Input*
   */
  public tagINCHI_Input pInp;
  /** 1 =&gt; the structure was marked as chiral, 2=&gt; not chiral, 0=&gt; not marked */
  public int bChiral;
  /** C type : char[256] */
  public byte[] szErrMsg = new byte[256];

  protected List<String> getFieldOrder() {
    return Arrays.asList("pInp", "bChiral", "szErrMsg");
  }
  /**
   * @param pInp a pointer to pInp that has all items 0 or NULL<br>
   * C type : inchi_Input*<br>
   * @param bChiral 1 =&gt; the structure was marked as chiral, 2=&gt; not chiral, 0=&gt; not marked<br>
   * @param szErrMsg C type : char[256]
   */
  public tagInchiInpData(tagINCHI_Input pInp, int bChiral, byte szErrMsg[]) {
    super();
    this.pInp = pInp;
    this.bChiral = bChiral;
    if ((szErrMsg.length != this.szErrMsg.length)) 
      throw new IllegalArgumentException("Wrong array size !");
    this.szErrMsg = szErrMsg;
  }
  
  public tagInchiInpData(tagINCHI_Input pInp) {
    super();
    this.pInp = pInp;
  }

}
