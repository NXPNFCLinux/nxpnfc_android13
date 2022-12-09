/*
*                    Copyright 2021 NXP
*
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

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.FormatException;
import com.nxp.nfc.NxpNfcAdapter;
import com.nxp.t4tdemo.Utils;
import android.nfc.NdefRecord;
import android.util.Log;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;

public class T4TFromNfccReaderWriter {
  private static String TAG = "T4TFromNfccReaderWriter";

  private Context mContext;
  private NfcAdapter mNfcAdapter;
  private NxpNfcAdapter mNxpNfcAdapter;
  private OnActionCompleteListener mOnActionCompleteListener;

  public T4TFromNfccReaderWriter(Context cntx) {
    this.mContext = cntx;
    this.mNfcAdapter = NfcAdapter.getDefaultAdapter(cntx);
    this.mNxpNfcAdapter = NxpNfcAdapter.getNxpNfcAdapter(mNfcAdapter);
    Log.e(TAG, "adapter loaded");
  }

  public void setActionListener(OnActionCompleteListener onActionCompleteListener){
    this.mOnActionCompleteListener = onActionCompleteListener;
  }

  static byte[] hexStringToBytes(String s) {
    if (s == null || s.length() == 0)
      return null;
    int len = s.length();
    if (len % 2 != 0) {
      s = '0' + s;
      len++;
    }
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] =
          (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  public static void printLongAPDU(String str) {
    if (str.length() > 4000) {
      Log.i(TAG, "APDU + : " + str.substring(0, 4000));
      printLongAPDU(str.substring(4000));
    } else
      Log.i(TAG, "APDU : " + str);
  }

  public void writeT4T(String content){

    try{
      byte[] fileId = { (byte)0xE1, (byte)0x04};
      final NdefRecord ndefRecord = NdefRecord.createTextRecord("en-us",content);
      byte[] tbytes = ndefRecord.toByteArray();

      Log.d(TAG, "NdefRecord getByte :" + Utils.toHexString(tbytes, 0, tbytes.length));
      Thread thrd = new Thread(new Runnable() {
          @Override
          public void run() {
            byte[] bytes = ndefRecord.toByteArray();
            Log.d(TAG, "NdefRecord getByte :" + Utils.toHexString(bytes, 0, bytes.length));
            int retValue = mNxpNfcAdapter.doWriteT4tData(fileId, bytes, bytes.length);
            Log.d(TAG, "T4T NDEF Write API Return Value :" + retValue);
            String status;
            if(retValue>=0){
              status = "T4T Write is success, doWriteT4tData API retunes : "+retValue;
            }else{
              status = "T4T Write is failed, doWriteT4tData API retunes : "+retValue;
            }
            mOnActionCompleteListener.OnT4tWriteComplete(status);
          }
      });
      thrd.start();
    }catch(IllegalArgumentException exception){
      mOnActionCompleteListener.OnT4tWriteComplete("UnsupportedEncodingException");
    }
  }

  public void readT4T(){

    byte[] fileId = { (byte)0xE1, (byte)0x04};

    Thread thrd = new Thread(new Runnable() {
        @Override
        public void run() {
          byte[] rData = mNxpNfcAdapter.doReadT4tData(fileId);
          if (rData != null) {
            try{
              NdefRecord record = new NdefRecord(rData);
              byte[] payloadData = record.getPayload();
              if(payloadData.length>0 && record.getTnf() == NdefRecord. TNF_WELL_KNOWN && Arrays.equals(NdefRecord.RTD_TEXT,record.getType())){
                int languageCodeLenth = payloadData[0];
                byte[] languageCode = new byte[languageCodeLenth];
                System.arraycopy(payloadData, 1, languageCode, 0, languageCodeLenth);
                int contentLength = payloadData.length-languageCodeLenth-1;
                byte[] contentBytes = new byte[contentLength];
                System.arraycopy(payloadData, languageCodeLenth+1, contentBytes, 0, contentLength);
                String content = new String(contentBytes,"UTF-8");

                mOnActionCompleteListener.OnT4TReadComplete(content,"Read success");
                Log.d(TAG, "T4T NDEF Read API Return Value : " + content);
              }else{
                mOnActionCompleteListener.OnT4TReadComplete("","Unable to read content");
                Log.d(TAG, "T4T NDEF Read API unable to read");
              }
            } catch(FormatException exception){
              mOnActionCompleteListener.OnT4tWriteComplete("Failed due to FormatException");
            } catch(UnsupportedEncodingException exception){
              mOnActionCompleteListener.OnT4tWriteComplete("Failed due to UnsupportedEncodingException");
            }
          }else{
            byte[] errorCode = {(byte) 0x64, (byte) 0xFF};
            Log.d(TAG, "T4T NDEF Read API Return Value : " + Utils.toHexString(errorCode, 0, errorCode.length));
            mOnActionCompleteListener.OnT4TReadComplete("","Read failed");
          }
      }
    });
    thrd.start();
  }

  interface OnActionCompleteListener{
    public void OnT4TReadComplete(String content,String status);
    public void OnT4tWriteComplete(String status);
  }

}
