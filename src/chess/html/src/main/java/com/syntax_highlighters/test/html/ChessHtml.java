package com.syntax_highlighters.test.html;

import com.syntax_highlighters.test.core.Chess;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class ChessHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Chess();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}
