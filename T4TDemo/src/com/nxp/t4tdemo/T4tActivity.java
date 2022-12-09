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

import android.app.*;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import com.nxp.t4tdemo.T4TFromNfccReaderWriter;
import com.nxp.t4tdemo.T4TFromNfccReaderWriter.OnActionCompleteListener;


public class T4tActivity extends Activity {

  private static final String TAG = "T4tActivity";
  private TextView mTxtStatus;
  private EditText mEdtContent;
  private Button mBtnWrite, mBtnRead, mBtnReset;
  private T4TFromNfccReaderWriter mT4TFromNfccReaderWriter;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate...");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initViews();
    initListeners();
    initNfcReferences();

    setTitle(getString(R.string.app_name));

  }

  public void initNfcReferences(){
    mT4TFromNfccReaderWriter = new T4TFromNfccReaderWriter(this);
    mT4TFromNfccReaderWriter.setActionListener(mOnActionCompleteListener);
  }

  public void initViews(){
    mTxtStatus = (TextView) findViewById(R.id.txt_status);
    mEdtContent = (EditText) findViewById(R.id.edt_content);
    mBtnWrite = (Button) findViewById(R.id.btn_write);
    mBtnRead = (Button) findViewById(R.id.btn_read);
    mBtnReset = (Button) findViewById(R.id.btn_reset);

    mTxtStatus.setText("Welcome !!!");
  }

  public void initListeners(){
    mBtnWrite.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        setStatusMessage("Write in progress");
        mT4TFromNfccReaderWriter.writeT4T(getEditContent());
      }
    });

    mBtnRead.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        setEditContent("");
        setStatusMessage("Read in progress");
        mT4TFromNfccReaderWriter.readT4T();
      }
    });

    mBtnReset.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        setEditContent("");
      }
    });

  }

  public void setStatusMessage(final String status) {

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mTxtStatus.setText(status);
      }
    });

  }

  public String getEditContent() {
    return mEdtContent.getText().toString();
  }

  public void setEditContent(final String content) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mEdtContent.setText(content);
      }
    });
  }

  public T4TFromNfccReaderWriter.OnActionCompleteListener mOnActionCompleteListener = new T4TFromNfccReaderWriter.OnActionCompleteListener() {
    public void OnT4TReadComplete(String content, String status) {
      setEditContent(content);
      setStatusMessage(status);
    }

    public void OnT4tWriteComplete(String status) {
      setStatusMessage(status);
    }
  };
}
