package com.syntax_highlighters.chess;

/**
 * Interface for async players players.
 *
 * Wraps a player (AI, online, etc.) that needs to be polled for updates
 * rather than have a blocking interface.
 */
public class AsyncPlayer {
    private final Wrapper wrapper;

    AsyncPlayer(IBlockingPlayer p) {
        wrapper = new Wrapper(p);
    }

    /**
     * Get a move synchronously from the player.
     * @param game The game to work with.
     * @return The received move or null.
     */
    Move getMove(AbstractGame game) {
        return wrapper.getMoveSynchronous(game);
    }

    /**
     * Get a move asynchronously from the player.
     * @param game The game to work with.
     * @return The received move or null.
     */
    Move pollMove(AbstractGame game) {
        switch (wrapper.getState()) {
            case Done: return wrapper.getResult();
            case Waiting: wrapper.startProcess(game);
            default: return null;
        }
    }

    /**
     * Returns true if the player can no longer perform moves.
     * @return true if the player can no longer perform moves.
     */
    private boolean hasCrashed() {
        return wrapper.getEx() == null;
    }

    /**
     * Get a friendly error message if the player has crashed.
     * @return The error message.
     */
    public String getError() {
        return hasCrashed() ? wrapper.getEx().getMessage() : null;
    }
}

class Wrapper {
    private State state = State.Waiting;
    private Move result = null;
    private final IBlockingPlayer player;
    private Exception ex;

    Wrapper(IBlockingPlayer player) {
        this.player = player;
    }

    Move getMoveSynchronous(AbstractGame game) {
        return player.GetMove(game);
    }

    void startProcess(AbstractGame game) {
        if (state != State.Waiting) return;
        state = State.Runnning;
        new Thread(() -> {
            try {
                result = player.GetMove(game);
            } catch (Exception ex) {
                this.ex = ex;
            } finally {
                state = State.Done;
            }
        }).start();
    }

    public State getState() {
        return state;
    }

    Move getResult() {
        if (state == State.Done) {
            Move r = result;
            result = null;
            state = State.Waiting;
            return r;
        }
        else return null;
    }

    Exception getEx() {
        return ex;
    }

    public enum State {
        Waiting,
        Runnning,
        Done,
    }
}