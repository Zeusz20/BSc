package com.zeusz.bsc.app.network;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.ui.Dialog;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Cloud;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.net.URLEncoder;


public class GameClient extends Channel {

    /* Static functionality */

    /**
     * @param state
     *  In which state the client will be after creation.
     * @param action
     *  Which action will the client perform (host or join).
     * @param initial
     *  The initial message to the server. This holds the name of the projects
     *  in case of hosting a game; and the ID of the game in case of joining to one.
     * */
    private static void launch(Activity ctx, Project project, State state, String action, String initial) {
        new Thread(new Task(ctx, () -> {
            MainActivity activity = (MainActivity) ctx;
            GameClient client = new GameClient(ctx, project);

            activity.setGameClient(client);
            client.setState(state);
            client.connect();   // connect to server
            client.listen();    // wait for response from server
            client.handshake(action, initial);   // send intent (create/join game) and initial message (project name/game id)
        })).start();
    }

    public static void createGame(Activity ctx, Project project) {
        if(Channel.isAvailable(ctx))
            launch(ctx, project, State.CREATE, SERVER_INFO.getString("create"), project.getSource().getName());
    }

    public static void joinGame(Activity ctx, String id) {
        if(!Channel.isAvailable(ctx)) return;

        GameClient client = ((MainActivity) ctx).getGameClient();

        if(client == null) {
            launch(ctx, null, State.JOIN, SERVER_INFO.getString("join"), id);
        }
        else {
            // client is already running
            // this is needed when the player enters a wrong game id, so the client is not destroyed and can be reused
            new Thread(new Task(ctx, () -> client.handshake(SERVER_INFO.getString("join"), id))).start();
        }
    }

    /* Client states */
    public enum State { CREATE, WAITING, JOIN, FILE_DOWNLOAD, IN_GAME, EXIT }

    /* Class fields and methods */
    protected Game game;

    protected String id;
    protected State state;
    protected boolean isHost;

    public GameClient(Activity ctx, Project project) throws Exception {
        super(ctx);
        this.game = new Game(project);
    }

    public void setMeta(boolean isHost, String id) {
        this.isHost = isHost;
        this.id = id;
    }

    public void setState(State state) { this.state = state; }

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public boolean isDirty() { return state != State.EXIT; }

    @Override
    public void parse(String response) throws Exception {
        synchronized(Channel.LOCK) {
            if(response == null) return;

            if(SERVER_INFO.getString("disconnect").equals(response)) {
                ctx.setGameClient(null);
                game.exit(ctx, isDirty());
            }
            else {
                switch(state) {
                    case CREATE:
                    case JOIN:
                        // the activity which creates the game is the host
                        init((state == State.CREATE), response);
                        break;
                    case WAITING:
                        wait(response);
                        break;
                    case FILE_DOWNLOAD:
                        download(response);
                        break;
                    case IN_GAME:
                        game.update(ctx, new Dictionary(response));
                        break;
                }
            }
        }
    }

    /** @return The base structure of a request. */
    protected Dictionary getMessage() throws Exception {
        Dictionary dictionary = new Dictionary(null);
        dictionary.put("is_host", isHost);
        dictionary.put("game_id", id);

        return dictionary;
    }

    /**
     * Initializes the game and the client when it is in the state of CREATE or JOIN.
     * After initialization the host is put to the WAITING state, while the
     * joining player is put in the FILE_DOWNLOAD state.
     * */
    protected void init(boolean isHost, String id) {
        if(id.equals(SERVER_INFO.getString("invalid"))) {
            Dialog.toast(ctx, Localization.localize("game.invalid"));
            return;
        }

        setMeta(isHost, id);
        setState(isHost ? State.WAITING : State.FILE_DOWNLOAD);

        // render waiting screen
        if(isHost)
            game.loadingScreen(ctx);
    }

    /**
     * While client is in the WAITING state, it waits for the other player to
     * download the project file (if not present on their machine).
     * The connection is established when the server sends the "ready" response.
     * */
    protected void wait(String response) throws Exception {
        if(SERVER_INFO.getString("ready").equals(response))
            load(null);  // start game
    }

    /**
     * In the FILE_DOWNLOAD state the joining player downloads the project
     * file to their machine if not present. After the download finished the
     * {@link #load(String)} method will load the project and the client's
     * state will be changed to IN_GAME.
     * */
    protected void download(String filename) throws Exception {
        if(!isHost) {
            String encoded = URLEncoder.encode(filename, SERVER_INFO.getString("format"));
            String url = Cloud.getCloudUrl("/projects/" + encoded);
            IOManager.download(ctx, url);  // download project file if missing
        }
    }

    /**
     * Loads the project to memory.
     * If joining player doesn't have the project file, this method is invoked
     * by the {@link DownloadReceiver} class when the project is finished downloading.
     * */
    public void load(String filename) throws Exception {
        // host already has the project loaded
        if(!isHost) {
            game.loadProject(ctx, filename);
            send(SERVER_INFO.getString("ready"));
            send(id);   // needed for identification
        }

        setState(State.IN_GAME);
        game.start(ctx);
    }

}
