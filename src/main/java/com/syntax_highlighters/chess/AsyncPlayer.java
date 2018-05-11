package com.syntax_highlighters.chess;

import com.syntax_highlighters.chess.game.AbstractGame;
import com.syntax_highlighters.chess.move.Move;

/**
 * Interface for async players players.
 *
 * Wraps a player (AI, online, etc.) that needs to be polled for updates
 * rather than have a blocking interface.
 */
public class AsyncPlayer {
    private final Wrapper wrapper;

    /**
     * Create an AsyncPlayer wrapping a blocking player.
     */
    public AsyncPlayer(IBlockingPlayer p) {
        wrapper = new Wrapper(p);
    }

    /**
     * Get a move synchronously from the player.
     * @param game The game to work with.
     * @return The received move or null.
     */
    public Move getMove(AbstractGame game) {
        return wrapper.getMoveSynchronous(game);
    }

    /**
     * Get a move asynchronously from the player.
     * @param game The game to work with.
     * @return The received move or null.
     */
    public Move pollMove(AbstractGame game) {
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

/**
 * Helper class: wrapper for the blocking player interface.
 */
class Wrapper {
    private State state = State.Waiting;
    private Move result = null;
    private final IBlockingPlayer player;
    private Exception ex;

    /**
     * Construct a new Wrapper.
     *
     * @param player The blocking player to wrap
     */
    Wrapper(IBlockingPlayer player) {
        this.player = player;
    }

    /**
     * Get a move in a synchronous manner.
     *
     * Presumably assumes that the player *can* perform a move.
     *
     * @param game The current game state
     * @return The move chosen by the blocking player
     */
    Move getMoveSynchronous(AbstractGame game) {
        return player.GetMove(game);
    }

    /**
     * Start a new thread where the blocking player attempts to retrieve a move.
     *
     * Sets the state of the wrapper.
     *
     * @param game The game state
     */
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

    /**
     * Determine the wrapper state.
     *
     * @return The current state of the player attempting to get a move
     */
    public State getState() {
        return state;
    }

    /**
     * Get the resulting move from the player.
     *
     * @return The move the player made, or null if either no move has been made
     * or the process of getting a move has not yet started.
     */
    Move getResult() {
        if (state == State.Done) {
            Move r = result;
            result = null;
            state = State.Waiting;
            return r;
        }
        else return null;
    }

    /**
     * Get the last exception that was thrown.
     * 
     * Makes it possible to figure out what went wrong and display a helpful
     * message in the UI, while handling errors in a graceful (non-crashing)
     * manner.
     *
     * @return The last thrown exception
     */
    Exception getEx() {
        return ex;
    }

    /**
     * State of the wrapper: determines which stage of the move getting process
     * it is in.
     */
    public enum State {
        Waiting,
        Runnning,
        Done,
    }
}
