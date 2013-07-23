package com.example.android.networkbasic;

import android.support.v4.app.Fragment;

import com.example.android.networkbasic.R;
import com.example.android.common.logger.LogView;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Simple fragment that contains a LogView and uses it to output log data it receives
 * through the LogNode interface.
 */
public class LogFragment extends Fragment {

  private LogView mLogView;

  public LogFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

      View result = inflater.inflate(R.layout.log_fragment, container, false);

      mLogView = (LogView) result.findViewById(R.id.sample_output);

      // Wire up so when the text changes, the view scrolls down.
      final ScrollView scrollView =
              ((ScrollView) result.findViewById(R.id.log_scroll));

      mLogView.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}

          @Override
          public void afterTextChanged(Editable s) {
              scrollView.fullScroll(ScrollView.FOCUS_DOWN);
          }
      });

      return result;
  }

  public LogView getLogView() {
      return mLogView;
  }

}
