package com.syntax_highlighters.test.android;

import com.syntax_highlighters.test.core.Chess;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class ChessActivity extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			initialize(new Chess(), config);
	}
}
