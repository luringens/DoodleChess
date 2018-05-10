package com.syntax_highlighters.chess.gui.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ConfirmationOverlay extends AbstractOverlay {
    private Button confirmButton;
    private Button cancelButton;
    
    /**
     * Create a new ConfirmationOverlay.
     *
     * Only for use within the Builder; not accessible from the outside.
     *
     * @param title The title of the overlay
     * @param assetManager The asset manager of the game
     */
    private ConfirmationOverlay(String title, AssetManager assetManager) {
        super(title, assetManager);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) return;
        stage.addActor(confirmButton);
        stage.addActor(cancelButton);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        confirmButton.setVisible(visible);
        cancelButton.setVisible(visible);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,1);
        super.draw(batch, parentAlpha);

        confirmButton.setPosition(getX() + getWidth()/2.f - confirmButton.getWidth(),
                getY() + 50.f);

        cancelButton.setPosition(getX() + getWidth()/2.f, getY() + 50.f);
    }

    /**
     * Confirmation dialog builder.
     */
    public static class Builder {
        private AssetManager assetManager;
        
        private String titleContent;
        private String confirmTextContent = "Confirm"; // default text for confirm button
        private String cancelTextContent = "Cancel";   // default text for cancel button

        private Stage actorStage;

        private boolean visibility = true;
        
        private Button.Callback confirmCallbackFunction;
        private Button.Callback cancelCallbackFunction;
        
        /**
         * Create a Builder with the given AssetManager.
         *
         * Note that this does not right away create the overlay; there are
         * still methods that need to be called first.
         *
         * @param a The game's AssetManager
         */
        public Builder(AssetManager a) {
            assetManager = a;
        }

        public Builder title(String t) {
            titleContent = t;
            return this;
        }

        public Builder confirmText(String t) {
            confirmTextContent = t;
            return this;
        }

        public Builder cancelText(String t) {
            cancelTextContent = t;
            return this;
        }

        public Builder visible(boolean v) {
            visibility = v;
            return this;
        }

        public Builder confirmCallback(Button.Callback c) {
            confirmCallbackFunction = c;
            return this;
        }

        public Builder cancelCallback(Button.Callback c) {
            cancelCallbackFunction = c;
            return this;
        }

        public Builder stage(Stage s) {
            actorStage = s;
            return this;
        }

        /**
         * Create the specified ConfirmationOverlay.
         * 
         * Note that this method throws an exception if the required information
         * is not in place - the required information is considered to be the
         * title of the overlay and the text on the two buttons. However, since
         * the button texts have default values, the only way for those to be
         * null is if the caller explicitly sets them - however, the
         * IllegalStateException is still thrown as a form of sanity check.
         *
         * @throws IllegalStateException if either the title, confirmation
         * button text, or cancel button text is null
         * @return The specified ConfirmationOvelay
         */
        public ConfirmationOverlay create() {
            // checking that all the required things are in place
            if (!(titleContent != null)) {
                throw new IllegalStateException("title must be specified");
            }
            if (!(confirmTextContent != null)) {
                throw new IllegalStateException("confirmText must be specified");
            }
            if (!(cancelTextContent != null)) {
                throw new IllegalStateException("cancelText must be specified");
            }
            
            ConfirmationOverlay o = new ConfirmationOverlay(titleContent, assetManager);
            o.confirmButton = new Button.Builder(confirmTextContent, assetManager)
                .size(200, 75)
                .callback(confirmCallbackFunction)
                .callback(() -> o.setVisible(false)) // hide this overlay when button clicked
                .visible(visibility)
                .create();
            o.cancelButton = new Button.Builder(cancelTextContent, assetManager)
                .size(200, 75)
                .callback(cancelCallbackFunction)
                .callback(() -> o.setVisible(false)) // hide this overlay when button clicked
                .visible(visibility)
                .create();
            
            if (actorStage != null) {
                actorStage.addActor(o);
            }

            o.setVisible(visibility);

            return o;
        }
    }
}
