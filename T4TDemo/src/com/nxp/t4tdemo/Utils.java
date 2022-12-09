/*
*                    Copyright (c), NXP Semiconductors
*
*                       (C)NXP Semiconductors B.V.2014-2018
*         All rights are reserved. Reproduction in whole or in part is
*        prohibited without the written consent of the copyright owner.
*    NXP reserves the right to make changes without notice at any time.
*   NXP makes no warranty, expressed, implied or statutory, including but
*   not limited to any implied warranty of merchantability or fitness for any
*  particular purpose, or that the use will not infringe any third party patent,
*   copyright or trademark. NXP must not be liable for any loss or damage
*                            arising from its use.
*
*/

package com.nxp.t4tdemo;
import android.util.Log;

public class Utils {
  public static byte[] hexStringToBytes(String hexString) {
    int stringLen = hexString.length();
    byte[] temp = new byte[stringLen];
    int resultLength = 0;
    int nibble = 0, i;
    byte nextByte = 0;
    for (i = 0; i < stringLen; i++) {
      char c = hexString.charAt(i);
      byte b = (byte) Character.digit(c, 16);

      if (b == -1) {
        if (!Character.isWhitespace(c))
          throw new IllegalArgumentException("Not HexString character: " + c);
        continue;
      }
      if (nibble == 0) {
        nextByte = (byte) (b << 4);
        nibble = 1;
      } else {
        nextByte |= b;
        temp[resultLength++] = nextByte;
        nibble = 0;
      }
    }

    if (nibble != 0) {
      throw new IllegalArgumentException("odd number of characters.");
    } else {
      byte[] result = new byte[resultLength];
      System.arraycopy(temp, 0, result, 0, resultLength);
      return result;
    }
  }

  public static String toHexString(byte[] buffer, int offset, int length) {
    if (length <= 0) {
      return new String("No Data : length < = 0");
    } else {
      final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
      char[] chars = new char[3 * length];
      for (int j = offset; j < offset + length; ++j) {
        chars[3 * (j - offset)] = HEX_CHARS[(buffer[j] & 0xF0) >>> 4];
        chars[3 * (j - offset) + 1] = HEX_CHARS[buffer[j] & 0x0F];
        chars[3 * (j - offset) + 2] = ' ';
      }
      return new String(chars);
    }
  }

  public static String bytArrayToHex(byte[] a) {
    StringBuilder sb = new StringBuilder();
    for (byte b : a) sb.append(String.format("%02X", b & 0xff));
    return sb.toString();
  }

  public static byte[] append(byte[] a, byte[] b) {
    byte[] result = new byte[a.length + b.length];
    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, b.length);
    return result;
  }

  public static byte[] extract(byte[] buffer, int offset, int length) {
    byte[] result = new byte[length];
    System.arraycopy(buffer, offset, result, 0, length);
    return result;
  }

  public static int byteArrToInt(byte lsB, byte msB) {
    byte[] lenBytes = new byte[2];
    int intval = 0;
    lenBytes[0] = lsB;
    lenBytes[1] = msB;
    String s = Utils.bytArrayToHex(lenBytes);
    try {
      intval = Integer.parseInt(s, 16);
      Log.d("byteArrToInt", "<<<<< dataLen : " + intval);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return intval;
  }
}
